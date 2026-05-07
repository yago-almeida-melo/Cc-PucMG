import os
import re
import time
import threading
import queue
from dataclasses import dataclass

import tkinter as tk
from tkinter import ttk, filedialog, messagebox

import numpy as np
import cv2
from PIL import Image, ImageTk

import torch
import torch.nn as nn
from torch.utils.data import Dataset, DataLoader
from torchvision import models, transforms

GROUP_MEMBERS = "Andre Luis Silva de Paula, Ricardo Soares Cerqueira, Yago Almeida Melo"

CLASS_MAP_4 = {"D": 0, "E": 1, "F": 2, "G": 3}
CLASS_NAMES_4 = ["BIRADS I", "BIRADS II", "BIRADS III", "BIRADS IV"]


def read_image_grayscale(path: str) -> np.ndarray:
	img = cv2.imread(path, cv2.IMREAD_UNCHANGED)
	if img is None:
		raise ValueError(f"Could not read image: {path}")
	if img.ndim == 3:
		img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
	return img


def normalize_to_uint8(img: np.ndarray) -> np.ndarray:
	if img.dtype == np.uint8:
		return img
	img_float = img.astype(np.float32)
	min_val = float(img_float.min())
	max_val = float(img_float.max())
	if max_val - min_val < 1e-6:
		return np.zeros_like(img_float, dtype=np.uint8)
	scaled = (img_float - min_val) / (max_val - min_val)
	return (scaled * 255.0).clip(0, 255).astype(np.uint8)


def largest_connected_component(mask: np.ndarray) -> np.ndarray:
	num_labels, labels, stats, _ = cv2.connectedComponentsWithStats(mask, connectivity=8)
	if num_labels <= 1:
		return mask
	largest_idx = 1 + np.argmax(stats[1:, cv2.CC_STAT_AREA])
	output = (labels == largest_idx).astype(np.uint8) * 255
	return output


def fill_holes(mask: np.ndarray) -> np.ndarray:
	h, w = mask.shape[:2]
	flood = mask.copy()
	mask_ff = np.zeros((h + 2, w + 2), np.uint8)
	cv2.floodFill(flood, mask_ff, (0, 0), 255)
	inv = cv2.bitwise_not(flood)
	return cv2.bitwise_or(mask, inv)


def segment_breast(img: np.ndarray) -> np.ndarray:
	img_u8 = normalize_to_uint8(img)
	blur = cv2.GaussianBlur(img_u8, (5, 5), 0)
	_, th = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
	if np.mean(th) > 127:
		th = cv2.bitwise_not(th)

	kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (7, 7))
	th = cv2.morphologyEx(th, cv2.MORPH_CLOSE, kernel, iterations=2)
	th = cv2.morphologyEx(th, cv2.MORPH_OPEN, kernel, iterations=1)
	th = largest_connected_component(th)
	th = fill_holes(th)
	mask = (th > 0).astype(np.uint8)
	return mask


def apply_mask(img: np.ndarray, mask: np.ndarray) -> np.ndarray:
	out = img.copy()
	out[mask == 0] = 0
	return out


def parse_class_label(path: str) -> int:
	base = os.path.basename(path).upper()
	for ch in base:
		if ch in CLASS_MAP_4:
			return CLASS_MAP_4[ch]
	raise ValueError(f"Class label not found in filename: {base}")


def parse_split_index(path: str) -> int:
	base = os.path.basename(path)
	numbers = re.findall(r"\d+", base)
	if not numbers:
		return -1
	return int(numbers[-1])


def build_file_list(base_dir: str):
	allowed = {".png", ".tif", ".tiff"}
	all_files = []
	for root, _, files in os.walk(base_dir):
		for name in files:
			ext = os.path.splitext(name)[1].lower()
			if ext in allowed:
				all_files.append(os.path.join(root, name))
	return all_files


def split_train_test(files):
	train, test = [], []
	for path in files:
		try:
			idx = parse_split_index(path)
			if idx != -1 and idx % 4 == 0:
				test.append(path)
			else:
				train.append(path)
		except Exception:
			train.append(path)
	return train, test


@dataclass
class DatasetConfig:
	model_name: str
	binary: bool
	use_segment: bool


class MammogramDataset(Dataset):
	def __init__(self, items, transform, use_segment, binary):
		self.items = items
		self.transform = transform
		self.use_segment = use_segment
		self.binary = binary

	def __len__(self):
		return len(self.items)

	def __getitem__(self, idx):
		path, label, angle = self.items[idx]
		img = read_image_grayscale(path)
		if self.use_segment:
			mask = segment_breast(img)
			img = apply_mask(img, mask)

		img_u8 = normalize_to_uint8(img)
		img_rgb = cv2.cvtColor(img_u8, cv2.COLOR_GRAY2RGB)
		pil = Image.fromarray(img_rgb)

		if angle != 0:
			pil = pil.rotate(angle, resample=Image.BILINEAR)

		if self.transform is not None:
			tensor = self.transform(pil)
		else:
			tensor = transforms.ToTensor()(pil)

		if self.binary:
			label = 0 if label in (0, 1) else 1

		return tensor, label


def build_items(files, augment):
	items = []
	angles = [-20, -10, 0, 10, 20] if augment else [0]
	for path in files:
		try:
			label = parse_class_label(path)
		except Exception:
			continue
		for angle in angles:
			items.append((path, label, angle))
	return items


def get_model(model_name: str, num_classes: int):
	if model_name == "DenseNet121":
		try:
			weights = models.DenseNet121_Weights.DEFAULT
			model = models.densenet121(weights=weights)
		except Exception:
			model = models.densenet121(weights=None)
		in_features = model.classifier.in_features
		model.classifier = nn.Linear(in_features, num_classes)
		input_size = 224
	else:
		try:
			weights = models.Inception_V3_Weights.DEFAULT
			model = models.inception_v3(weights=weights, aux_logits=False)
		except Exception:
			model = models.inception_v3(weights=None, aux_logits=False)
		in_features = model.fc.in_features
		model.fc = nn.Linear(in_features, num_classes)
		input_size = 299
	return model, input_size


def get_transforms(input_size: int):
	normalize = transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
	return transforms.Compose([
		transforms.Resize((input_size, input_size)),
		transforms.ToTensor(),
		normalize,
	])


def compute_binary_metrics(y_true, y_pred):
	tp = sum(1 for t, p in zip(y_true, y_pred) if t == 1 and p == 1)
	tn = sum(1 for t, p in zip(y_true, y_pred) if t == 0 and p == 0)
	fp = sum(1 for t, p in zip(y_true, y_pred) if t == 0 and p == 1)
	fn = sum(1 for t, p in zip(y_true, y_pred) if t == 1 and p == 0)

	sensitivity = tp / (tp + fn) if (tp + fn) else 0.0
	specificity = tn / (tn + fp) if (tn + fp) else 0.0
	precision = tp / (tp + fp) if (tp + fp) else 0.0
	accuracy = (tp + tn) / max(len(y_true), 1)
	f1 = (2 * precision * sensitivity / (precision + sensitivity)) if (precision + sensitivity) else 0.0
	return sensitivity, specificity, precision, accuracy, f1


def confusion_matrix(num_classes, y_true, y_pred):
	cm = np.zeros((num_classes, num_classes), dtype=np.int64)
	for t, p in zip(y_true, y_pred):
		cm[t, p] += 1
	return cm


def compute_multiclass_metrics(cm):
	num_classes = cm.shape[0]
	sensitivities = []
	specificities = []
	total = cm.sum()
	for c in range(num_classes):
		tp = cm[c, c]
		fn = cm[c, :].sum() - tp
		fp = cm[:, c].sum() - tp
		tn = total - (tp + fp + fn)
		sens = tp / (tp + fn) if (tp + fn) else 0.0
		spec = tn / (tn + fp) if (tn + fp) else 0.0
		sensitivities.append(sens)
		specificities.append(spec)
	return float(np.mean(sensitivities)), float(np.mean(specificities))


class GradCAM:
	def __init__(self, model, target_layer):
		self.model = model
		self.target_layer = target_layer
		self.activations = None
		self.gradients = None
		self._register_hooks()

	def _register_hooks(self):
		def forward_hook(_, __, output):
			self.activations = output.detach()

		def backward_hook(_, grad_input, grad_output):
			self.gradients = grad_output[0].detach()

		self.target_layer.register_forward_hook(forward_hook)
		self.target_layer.register_full_backward_hook(backward_hook)

	def generate(self, input_tensor, class_idx=None):
		self.model.zero_grad(set_to_none=True)
		outputs = self.model(input_tensor)
		if class_idx is None:
			class_idx = int(outputs.argmax(dim=1).item())
		loss = outputs[:, class_idx].sum()
		loss.backward()

		weights = self.gradients.mean(dim=(2, 3), keepdim=True)
		cam = (weights * self.activations).sum(dim=1, keepdim=True)
		cam = torch.relu(cam)
		cam = cam.squeeze(0).squeeze(0)
		cam = cam - cam.min()
		cam = cam / (cam.max() + 1e-6)
		return cam.cpu().numpy(), class_idx


def get_target_layer(model_name: str, model):
	if model_name == "DenseNet121":
		return model.features.denseblock4
	return model.Mixed_7c


class MammogramApp:
	def __init__(self, root):
		self.root = root
		self.root.title("Mammogram Density Classifier")
		self.root.geometry("1200x720")

		self.current_image_path = None
		self.current_image = None
		self.current_mask = None
		self.display_image = None
		self.zoom_factor = 1.0

		self.dataset_dir = None
		self.train_files = []
		self.test_files = []

		self.model = None
		self.model_name_var = tk.StringVar(value="DenseNet121")
		self.mode_var = tk.StringVar(value="binary")
		self.segment_var = tk.BooleanVar(value=True)
		self.epochs_var = tk.IntVar(value=3)
		self.batch_var = tk.IntVar(value=8)
		self.lr_var = tk.DoubleVar(value=1e-4)

		self.log_queue = queue.Queue()
		self._build_ui()
		self._schedule_log_flush()

	def _build_ui(self):
		main = ttk.Frame(self.root)
		main.pack(fill=tk.BOTH, expand=True)

		left = ttk.Frame(main)
		left.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)

		right = ttk.Frame(main, width=350)
		right.pack(side=tk.RIGHT, fill=tk.Y)

		canvas_frame = ttk.Frame(left)
		canvas_frame.pack(fill=tk.BOTH, expand=True)

		self.canvas = tk.Canvas(canvas_frame, bg="#111111", highlightthickness=0)
		self.canvas.grid(row=0, column=0, sticky="nsew")

		v_scroll = ttk.Scrollbar(canvas_frame, orient=tk.VERTICAL, command=self.canvas.yview)
		h_scroll = ttk.Scrollbar(canvas_frame, orient=tk.HORIZONTAL, command=self.canvas.xview)
		self.canvas.configure(yscrollcommand=v_scroll.set, xscrollcommand=h_scroll.set)

		v_scroll.grid(row=0, column=1, sticky="ns")
		h_scroll.grid(row=1, column=0, sticky="ew")
		canvas_frame.rowconfigure(0, weight=1)
		canvas_frame.columnconfigure(0, weight=1)

		controls = ttk.Frame(right)
		controls.pack(fill=tk.X, padx=8, pady=8)

		ttk.Button(controls, text="Load Image", command=self.load_image).pack(fill=tk.X, pady=2)
		ttk.Button(controls, text="Segment", command=self.segment_current).pack(fill=tk.X, pady=2)
		ttk.Button(controls, text="Zoom +", command=lambda: self.zoom(1.25)).pack(fill=tk.X, pady=2)
		ttk.Button(controls, text="Zoom -", command=lambda: self.zoom(0.8)).pack(fill=tk.X, pady=2)

		ttk.Separator(controls, orient=tk.HORIZONTAL).pack(fill=tk.X, pady=8)

		ttk.Button(controls, text="Dataset Dir", command=self.choose_dataset_dir).pack(fill=tk.X, pady=2)

		ttk.Label(controls, text="Model").pack(anchor=tk.W)
		ttk.Combobox(controls, textvariable=self.model_name_var, values=["DenseNet121", "InceptionV3"], state="readonly").pack(fill=tk.X)

		ttk.Label(controls, text="Mode").pack(anchor=tk.W)
		ttk.Combobox(controls, textvariable=self.mode_var, values=["binary", "4class"], state="readonly").pack(fill=tk.X)

		ttk.Checkbutton(controls, text="Use Segmented", variable=self.segment_var).pack(anchor=tk.W, pady=2)

		ttk.Label(controls, text="Epochs").pack(anchor=tk.W)
		ttk.Entry(controls, textvariable=self.epochs_var).pack(fill=tk.X)

		ttk.Label(controls, text="Batch Size").pack(anchor=tk.W)
		ttk.Entry(controls, textvariable=self.batch_var).pack(fill=tk.X)

		ttk.Label(controls, text="Learning Rate").pack(anchor=tk.W)
		ttk.Entry(controls, textvariable=self.lr_var).pack(fill=tk.X)

		ttk.Button(controls, text="Train", command=self.train_model).pack(fill=tk.X, pady=4)
		ttk.Button(controls, text="Evaluate", command=self.evaluate_model).pack(fill=tk.X, pady=4)
		ttk.Button(controls, text="Load Weights", command=self.load_weights).pack(fill=tk.X, pady=2)
		ttk.Button(controls, text="Save Weights", command=self.save_weights).pack(fill=tk.X, pady=2)

		ttk.Separator(controls, orient=tk.HORIZONTAL).pack(fill=tk.X, pady=8)

		ttk.Button(controls, text="Classify Current", command=self.classify_current).pack(fill=tk.X, pady=2)
		ttk.Button(controls, text="Grad-CAM", command=self.grad_cam_current).pack(fill=tk.X, pady=2)

		log_frame = ttk.Frame(right)
		log_frame.pack(fill=tk.BOTH, expand=True, padx=8, pady=8)
		ttk.Label(log_frame, text="Log").pack(anchor=tk.W)
		self.log_text = tk.Text(log_frame, height=20)
		self.log_text.pack(fill=tk.BOTH, expand=True)

	def log(self, message: str):
		self.log_queue.put(message)

	def _schedule_log_flush(self):
		while not self.log_queue.empty():
			msg = self.log_queue.get_nowait()
			self.log_text.insert(tk.END, msg + "\n")
			self.log_text.see(tk.END)
		self.root.after(100, self._schedule_log_flush)

	def load_image(self):
		path = filedialog.askopenfilename(filetypes=[
			("Image files", "*.png;*.tif;*.tiff"),
			("All files", "*.*")
		])
		if not path:
			return
		try:
			self.current_image = read_image_grayscale(path)
		except Exception as exc:
			messagebox.showerror("Error", str(exc))
			return
		self.current_image_path = path
		self.current_mask = None
		self.zoom_factor = 1.0
		self._render_image(self.current_image)
		self.log(f"Loaded image: {path}")

	def _render_image(self, img: np.ndarray):
		img_u8 = normalize_to_uint8(img)
		pil = Image.fromarray(img_u8)
		if self.zoom_factor != 1.0:
			w, h = pil.size
			pil = pil.resize((int(w * self.zoom_factor), int(h * self.zoom_factor)), Image.BILINEAR)
		self.display_image = ImageTk.PhotoImage(pil)
		self.canvas.delete("all")
		self.canvas.create_image(0, 0, anchor=tk.NW, image=self.display_image)
		self.canvas.config(scrollregion=self.canvas.bbox(tk.ALL))

	def zoom(self, factor: float):
		if self.current_image is None:
			return
		self.zoom_factor *= factor
		self.zoom_factor = max(0.1, min(self.zoom_factor, 5.0))
		self._render_image(self.current_image if self.current_mask is None else apply_mask(self.current_image, self.current_mask))

	def segment_current(self):
		if self.current_image is None:
			return
		self.current_mask = segment_breast(self.current_image)
		seg = apply_mask(self.current_image, self.current_mask)
		self._render_image(seg)
		self.log("Segmentation applied to current image")

	def choose_dataset_dir(self):
		path = filedialog.askdirectory()
		if not path:
			return
		self.dataset_dir = path
		files = build_file_list(path)
		train, test = split_train_test(files)
		self.train_files = train
		self.test_files = test
		self.log(f"Dataset dir: {path}")
		self.log(f"Train files: {len(train)} | Test files: {len(test)}")

	def _device(self):
		return torch.device("cuda" if torch.cuda.is_available() else "cpu")

	def _build_dataloaders(self, config: DatasetConfig):
		model, input_size = get_model(config.model_name, 2 if config.binary else 4)
		tform = get_transforms(input_size)

		train_items = build_items(self.train_files, augment=True)
		test_items = build_items(self.test_files, augment=False)

		train_ds = MammogramDataset(train_items, tform, config.use_segment, config.binary)
		test_ds = MammogramDataset(test_items, tform, config.use_segment, config.binary)

		train_loader = DataLoader(train_ds, batch_size=self.batch_var.get(), shuffle=True, num_workers=0)
		test_loader = DataLoader(test_ds, batch_size=self.batch_var.get(), shuffle=False, num_workers=0)
		return model, train_loader, test_loader

	def _train_worker(self, config: DatasetConfig):
		if not self.train_files:
			self.log("No train files. Select dataset dir first.")
			return
		self.log("Starting training...")
		device = self._device()

		model, train_loader, test_loader = self._build_dataloaders(config)
		num_classes = 2 if config.binary else 4

		for param in model.parameters():
			param.requires_grad = False

		if config.model_name == "DenseNet121":
			for param in model.classifier.parameters():
				param.requires_grad = True
		else:
			for param in model.fc.parameters():
				param.requires_grad = True

		model = model.to(device)
		optimizer = torch.optim.Adam(filter(lambda p: p.requires_grad, model.parameters()), lr=self.lr_var.get())
		criterion = nn.CrossEntropyLoss()

		epochs = self.epochs_var.get()
		for epoch in range(1, epochs + 1):
			model.train()
			running_loss = 0.0
			correct = 0
			total = 0
			for batch_idx, (inputs, labels) in enumerate(train_loader, start=1):
				inputs = inputs.to(device)
				labels = labels.to(device)

				optimizer.zero_grad(set_to_none=True)
				outputs = model(inputs)
				loss = criterion(outputs, labels)
				loss.backward()
				optimizer.step()

				running_loss += loss.item() * labels.size(0)
				preds = outputs.argmax(dim=1)
				correct += (preds == labels).sum().item()
				total += labels.size(0)

			epoch_loss = running_loss / max(total, 1)
			epoch_acc = correct / max(total, 1)
			self.log(f"Epoch {epoch}/{epochs} - Loss: {epoch_loss:.4f} Acc: {epoch_acc:.4f}")

		self.model = model
		self.log("Training done.")

		if self.test_files:
			self._evaluate_worker(config)

	def train_model(self):
		config = DatasetConfig(
			model_name=self.model_name_var.get(),
			binary=self.mode_var.get() == "binary",
			use_segment=self.segment_var.get(),
		)
		thread = threading.Thread(target=self._train_worker, args=(config,), daemon=True)
		thread.start()

	def _evaluate_worker(self, config: DatasetConfig):
		if self.model is None:
			self.log("No model loaded.")
			return
		if not self.test_files:
			self.log("No test files. Select dataset dir first.")
			return

		device = self._device()
		self.model = self.model.to(device)
		self.model.eval()

		_, _, test_loader = self._build_dataloaders(config)

		y_true = []
		y_pred = []
		start = time.perf_counter()
		with torch.no_grad():
			for inputs, labels in test_loader:
				inputs = inputs.to(device)
				labels = labels.to(device)
				outputs = self.model(inputs)
				preds = outputs.argmax(dim=1)
				y_true.extend(labels.cpu().tolist())
				y_pred.extend(preds.cpu().tolist())
		elapsed = time.perf_counter() - start

		if config.binary:
			sens, spec, prec, acc, f1 = compute_binary_metrics(y_true, y_pred)
			self.log(f"Binary results - Time: {elapsed:.2f}s")
			self.log(f"Sensitivity: {sens:.4f} | Specificity: {spec:.4f} | Precision: {prec:.4f}")
			self.log(f"Accuracy: {acc:.4f} | F1: {f1:.4f}")
		else:
			cm = confusion_matrix(4, y_true, y_pred)
			sens, spec = compute_multiclass_metrics(cm)
			self.log(f"4-class results - Time: {elapsed:.2f}s")
			self.log("Confusion Matrix:")
			self.log(str(cm))
			self.log(f"Avg Sensitivity: {sens:.4f} | Avg Specificity: {spec:.4f}")

	def evaluate_model(self):
		config = DatasetConfig(
			model_name=self.model_name_var.get(),
			binary=self.mode_var.get() == "binary",
			use_segment=self.segment_var.get(),
		)
		thread = threading.Thread(target=self._evaluate_worker, args=(config,), daemon=True)
		thread.start()

	def save_weights(self):
		if self.model is None:
			messagebox.showinfo("Info", "No model to save")
			return
		path = filedialog.asksaveasfilename(defaultextension=".pt", filetypes=[("PyTorch", "*.pt")])
		if not path:
			return
		data = {
			"model_state": self.model.state_dict(),
			"model_name": self.model_name_var.get(),
			"mode": self.mode_var.get(),
		}
		torch.save(data, path)
		self.log(f"Weights saved: {path}")

	def load_weights(self):
		path = filedialog.askopenfilename(filetypes=[("PyTorch", "*.pt"), ("All files", "*.*")])
		if not path:
			return
		data = torch.load(path, map_location="cpu")
		model_name = data.get("model_name", "DenseNet121")
		mode = data.get("mode", "binary")
		num_classes = 2 if mode == "binary" else 4
		model, _ = get_model(model_name, num_classes)
		model.load_state_dict(data["model_state"], strict=True)
		self.model = model
		self.model_name_var.set(model_name)
		self.mode_var.set(mode)
		self.log(f"Weights loaded: {path}")

	def classify_current(self):
		if self.model is None:
			self.log("No model loaded.")
			return
		if self.current_image is None:
			self.log("No image loaded.")
			return

		device = self._device()
		config = DatasetConfig(
			model_name=self.model_name_var.get(),
			binary=self.mode_var.get() == "binary",
			use_segment=self.segment_var.get(),
		)
		_, input_size = get_model(config.model_name, 2 if config.binary else 4)
		tform = get_transforms(input_size)

		img = self.current_image
		if config.use_segment:
			mask = segment_breast(img)
			img = apply_mask(img, mask)
		img_u8 = normalize_to_uint8(img)
		img_rgb = cv2.cvtColor(img_u8, cv2.COLOR_GRAY2RGB)
		pil = Image.fromarray(img_rgb)
		tensor = tform(pil).unsqueeze(0)

		self.model = self.model.to(device)
		self.model.eval()
		start = time.perf_counter()
		with torch.no_grad():
			outputs = self.model(tensor.to(device))
			pred = outputs.argmax(dim=1).item()
		elapsed = time.perf_counter() - start

		if config.binary:
			label = "I+II" if pred == 0 else "III+IV"
		else:
			label = CLASS_NAMES_4[pred]
		self.log(f"Prediction: {label} | Time: {elapsed:.4f}s")

	def grad_cam_current(self):
		if self.model is None:
			self.log("No model loaded.")
			return
		if self.current_image is None:
			self.log("No image loaded.")
			return

		device = self._device()
		config = DatasetConfig(
			model_name=self.model_name_var.get(),
			binary=self.mode_var.get() == "binary",
			use_segment=self.segment_var.get(),
		)
		_, input_size = get_model(config.model_name, 2 if config.binary else 4)
		tform = get_transforms(input_size)

		img = self.current_image
		if config.use_segment:
			mask = segment_breast(img)
			img = apply_mask(img, mask)
		img_u8 = normalize_to_uint8(img)
		img_rgb = cv2.cvtColor(img_u8, cv2.COLOR_GRAY2RGB)
		pil = Image.fromarray(img_rgb)
		pil_resized = pil.resize((input_size, input_size), Image.BILINEAR)
		tensor = tform(pil).unsqueeze(0)

		self.model = self.model.to(device)
		self.model.eval()
		target_layer = get_target_layer(config.model_name, self.model)
		cam_engine = GradCAM(self.model, target_layer)

		cam, class_idx = cam_engine.generate(tensor.to(device))
		cam = cv2.resize(cam, (input_size, input_size))
		heatmap = (cam * 255).astype(np.uint8)
		heatmap = cv2.applyColorMap(heatmap, cv2.COLORMAP_JET)
		base = np.array(pil_resized)
		overlay = cv2.addWeighted(base, 0.6, heatmap, 0.4, 0)
		self._render_image(cv2.cvtColor(overlay, cv2.COLOR_BGR2GRAY))
		if config.binary:
			label = "I+II" if class_idx == 0 else "III+IV"
		else:
			label = CLASS_NAMES_4[class_idx]
		self.log(f"Grad-CAM generated for class: {label}")


def main():
	root = tk.Tk()
	app = MammogramApp(root)
	app.log(f"Group members: {GROUP_MEMBERS}")
	root.mainloop()


if __name__ == "__main__":
	main()
