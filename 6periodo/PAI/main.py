"""
Image Processing Script with GUI
Provides various image processing operations using OpenCV and Pillow
"""

import cv2
import numpy as np
from PIL import Image, ImageEnhance, ImageFilter, ImageTk
import os
import tkinter as tk
from tkinter import ttk, filedialog, messagebox


class ImageProcessor:
    def __init__(self, image_path):
        """Initialize with image path"""
        self.image_path = image_path
        self.image_cv = cv2.imread(image_path)
        self.image_pil = Image.open(image_path)
        
        if self.image_cv is None:
            raise ValueError(f"Unable to load image: {image_path}")
    
    def show_image(self, image, window_name="Image"):
        """Display image using OpenCV"""
        cv2.imshow(window_name, image)
        cv2.waitKey(0)
        cv2.destroyAllWindows()
    
    def save_image(self, image, output_path, use_cv=True):
        """Save processed image"""
        if use_cv:
            cv2.imwrite(output_path, image)
        else:
            image.save(output_path)
        print(f"Image saved to: {output_path}")
    
    # Filtering Operations
    def apply_blur(self, kernel_size=5):
        """Apply Gaussian blur"""
        return cv2.GaussianBlur(self.image_cv, (kernel_size, kernel_size), 0)
    
    def apply_median_blur(self, kernel_size=5):
        """Apply median blur (good for salt-and-pepper noise)"""
        return cv2.medianBlur(self.image_cv, kernel_size)
    
    def apply_bilateral_filter(self, d=9, sigma_color=75, sigma_space=75):
        """Apply bilateral filter (preserves edges while smoothing)"""
        return cv2.bilateralFilter(self.image_cv, d, sigma_color, sigma_space)
    
    def sharpen(self):
        """Sharpen image"""
        kernel = np.array([[-1, -1, -1],
                          [-1,  9, -1],
                          [-1, -1, -1]])
        return cv2.filter2D(self.image_cv, -1, kernel)
    
    # Edge Detection
    def detect_edges_canny(self, threshold1=100, threshold2=200):
        """Detect edges using Canny algorithm"""
        gray = cv2.cvtColor(self.image_cv, cv2.COLOR_BGR2GRAY)
        return cv2.Canny(gray, threshold1, threshold2)
    
    def detect_edges_sobel(self):
        """Detect edges using Sobel operator"""
        gray = cv2.cvtColor(self.image_cv, cv2.COLOR_BGR2GRAY)
        sobelx = cv2.Sobel(gray, cv2.CV_64F, 1, 0, ksize=5)
        sobely = cv2.Sobel(gray, cv2.CV_64F, 0, 1, ksize=5)
        sobel = np.sqrt(sobelx**2 + sobely**2)
        return np.uint8(sobel)
    
    def detect_edges_laplacian(self):
        """Detect edges using Laplacian"""
        gray = cv2.cvtColor(self.image_cv, cv2.COLOR_BGR2GRAY)
        return cv2.Laplacian(gray, cv2.CV_64F)
    
    # Color Operations
    def convert_to_grayscale(self):
        """Convert image to grayscale"""
        return cv2.cvtColor(self.image_cv, cv2.COLOR_BGR2GRAY)
    
    def convert_to_hsv(self):
        """Convert image to HSV color space"""
        return cv2.cvtColor(self.image_cv, cv2.COLOR_BGR2HSV)
    
    def adjust_brightness(self, factor=1.5):
        """Adjust image brightness (factor > 1 brightens, < 1 darkens)"""
        enhancer = ImageEnhance.Brightness(self.image_pil)
        return enhancer.enhance(factor)
    
    def adjust_contrast(self, factor=1.5):
        """Adjust image contrast"""
        enhancer = ImageEnhance.Contrast(self.image_pil)
        return enhancer.enhance(factor)
    
    def adjust_saturation(self, factor=1.5):
        """Adjust color saturation"""
        enhancer = ImageEnhance.Color(self.image_pil)
        return enhancer.enhance(factor)
    
    # Thresholding
    def apply_threshold(self, threshold_value=127, max_value=255):
        """Apply binary threshold"""
        gray = cv2.cvtColor(self.image_cv, cv2.COLOR_BGR2GRAY)
        _, thresh = cv2.threshold(gray, threshold_value, max_value, cv2.THRESH_BINARY)
        return thresh
    
    def apply_adaptive_threshold(self, block_size=11, C=2):
        """Apply adaptive threshold"""
        gray = cv2.cvtColor(self.image_cv, cv2.COLOR_BGR2GRAY)
        return cv2.adaptiveThreshold(gray, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                                     cv2.THRESH_BINARY, block_size, C)
    
    def apply_otsu_threshold(self):
        """Apply Otsu's thresholding"""
        gray = cv2.cvtColor(self.image_cv, cv2.COLOR_BGR2GRAY)
        _, thresh = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
        return thresh
    
    # Morphological Operations
    def apply_erosion(self, kernel_size=5, iterations=1):
        """Apply erosion"""
        kernel = np.ones((kernel_size, kernel_size), np.uint8)
        return cv2.erode(self.image_cv, kernel, iterations=iterations)
    
    def apply_dilation(self, kernel_size=5, iterations=1):
        """Apply dilation"""
        kernel = np.ones((kernel_size, kernel_size), np.uint8)
        return cv2.dilate(self.image_cv, kernel, iterations=iterations)
    
    def apply_opening(self, kernel_size=5):
        """Apply morphological opening (erosion followed by dilation)"""
        kernel = np.ones((kernel_size, kernel_size), np.uint8)
        return cv2.morphologyEx(self.image_cv, cv2.MORPH_OPEN, kernel)
    
    def apply_closing(self, kernel_size=5):
        """Apply morphological closing (dilation followed by erosion)"""
        kernel = np.ones((kernel_size, kernel_size), np.uint8)
        return cv2.morphologyEx(self.image_cv, cv2.MORPH_CLOSE, kernel)
    
    # Transformations
    def rotate(self, angle):
        """Rotate image by specified angle"""
        height, width = self.image_cv.shape[:2]
        center = (width // 2, height // 2)
        matrix = cv2.getRotationMatrix2D(center, angle, 1.0)
        return cv2.warpAffine(self.image_cv, matrix, (width, height))
    
    def flip(self, mode='horizontal'):
        """Flip image (horizontal, vertical, or both)"""
        if mode == 'horizontal':
            return cv2.flip(self.image_cv, 1)
        elif mode == 'vertical':
            return cv2.flip(self.image_cv, 0)
        elif mode == 'both':
            return cv2.flip(self.image_cv, -1)
    
    def resize(self, width=None, height=None, scale=None):
        """Resize image"""
        if scale:
            width = int(self.image_cv.shape[1] * scale)
            height = int(self.image_cv.shape[0] * scale)
        elif width and height:
            pass
        else:
            raise ValueError("Provide either width and height, or scale")
        
        return cv2.resize(self.image_cv, (width, height), interpolation=cv2.INTER_LINEAR)
    
    def crop(self, x, y, width, height):
        """Crop image"""
        return self.image_cv[y:y+height, x:x+width]
    
    # Histogram Operations
    def equalize_histogram(self):
        """Equalize histogram for contrast enhancement"""
        gray = cv2.cvtColor(self.image_cv, cv2.COLOR_BGR2GRAY)
        return cv2.equalizeHist(gray)
    
    def show_histogram(self):
        """Show histogram of image"""
        color = ('b', 'g', 'r')
        for i, col in enumerate(color):
            hist = cv2.calcHist([self.image_cv], [i], None, [256], [0, 256])
            print(f"{col.upper()} channel histogram calculated")
        return hist
    
    # Image Info
    def get_info(self):
        """Get image information"""
        height, width = self.image_cv.shape[:2]
        channels = self.image_cv.shape[2] if len(self.image_cv.shape) == 3 else 1
        print(f"Image Information:")
        print(f"  Path: {self.image_path}")
        print(f"  Dimensions: {width}x{height}")
        print(f"  Channels: {channels}")
        print(f"  Size: {self.image_cv.size}")
        print(f"  Data type: {self.image_cv.dtype}")


def main():
    """Main function with GUI interface"""
    app = ImageProcessingGUI()
    app.mainloop()


class ImageProcessingGUI(tk.Tk):
    """GUI Application for Image Processing"""
    
    def __init__(self):
        super().__init__()
        
        self.title("Image Processing Tool")
        self.geometry("1200x800")
        
        # Variables
        self.processor = None
        self.original_image = None
        self.current_image = None
        self.display_image = None
        
        # Setup UI
        self.setup_ui()
        
    def setup_ui(self):
        """Setup the user interface"""
        # Main container
        main_container = ttk.Frame(self)
        main_container.pack(fill=tk.BOTH, expand=True, padx=10, pady=10)
        
        # Left panel - Controls
        left_panel = ttk.Frame(main_container, width=300)
        left_panel.pack(side=tk.LEFT, fill=tk.BOTH, padx=(0, 10))
        
        # Right panel - Image display
        right_panel = ttk.Frame(main_container)
        right_panel.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True)
        
        # File operations
        file_frame = ttk.LabelFrame(left_panel, text="File Operations", padding=10)
        file_frame.pack(fill=tk.X, pady=(0, 10))
        
        ttk.Button(file_frame, text="Load Image", command=self.load_image).pack(fill=tk.X, pady=2)
        ttk.Button(file_frame, text="Save Image", command=self.save_image).pack(fill=tk.X, pady=2)
        ttk.Button(file_frame, text="Reset to Original", command=self.reset_image).pack(fill=tk.X, pady=2)
        
        # Notebook for operations
        notebook = ttk.Notebook(left_panel)
        notebook.pack(fill=tk.BOTH, expand=True)
        
        # Tab 1: Filters
        filters_frame = ttk.Frame(notebook, padding=10)
        notebook.add(filters_frame, text="Filters")
        self.setup_filters_tab(filters_frame)
        
        # Tab 2: Color Adjustments
        color_frame = ttk.Frame(notebook, padding=10)
        notebook.add(color_frame, text="Colors")
        self.setup_color_tab(color_frame)
        
        # Tab 3: Effects
        effects_frame = ttk.Frame(notebook, padding=10)
        notebook.add(effects_frame, text="Effects")
        self.setup_effects_tab(effects_frame)
        
        # Tab 4: Transformations
        transform_frame = ttk.Frame(notebook, padding=10)
        notebook.add(transform_frame, text="Transform")
        self.setup_transform_tab(transform_frame)
        
        # Image display area
        self.canvas = tk.Canvas(right_panel, bg='gray')
        self.canvas.pack(fill=tk.BOTH, expand=True)
        
        # Status bar
        self.status_var = tk.StringVar(value="Load an image to begin")
        status_bar = ttk.Label(self, textvariable=self.status_var, relief=tk.SUNKEN)
        status_bar.pack(side=tk.BOTTOM, fill=tk.X)
        
    def setup_filters_tab(self, parent):
        """Setup filters tab"""
        # Blur
        ttk.Label(parent, text="Gaussian Blur:").pack(anchor=tk.W, pady=(0, 5))
        blur_frame = ttk.Frame(parent)
        blur_frame.pack(fill=tk.X, pady=(0, 10))
        
        self.blur_var = tk.IntVar(value=5)
        ttk.Scale(blur_frame, from_=1, to=31, variable=self.blur_var, 
                 orient=tk.HORIZONTAL, command=lambda x: self.blur_var.set(self.blur_var.get() | 1)).pack(side=tk.LEFT, fill=tk.X, expand=True)
        ttk.Label(blur_frame, textvariable=self.blur_var, width=4).pack(side=tk.RIGHT)
        ttk.Button(parent, text="Apply Blur", command=self.apply_blur).pack(fill=tk.X, pady=(0, 15))
        
        # Sharpen
        ttk.Button(parent, text="Sharpen", command=self.apply_sharpen).pack(fill=tk.X, pady=(0, 15))
        
        # Median Blur
        ttk.Label(parent, text="Median Blur:").pack(anchor=tk.W, pady=(0, 5))
        median_frame = ttk.Frame(parent)
        median_frame.pack(fill=tk.X, pady=(0, 10))
        
        self.median_var = tk.IntVar(value=5)
        ttk.Scale(median_frame, from_=1, to=31, variable=self.median_var,
                 orient=tk.HORIZONTAL, command=lambda x: self.median_var.set(self.median_var.get() | 1)).pack(side=tk.LEFT, fill=tk.X, expand=True)
        ttk.Label(median_frame, textvariable=self.median_var, width=4).pack(side=tk.RIGHT)
        ttk.Button(parent, text="Apply Median Blur", command=self.apply_median_blur).pack(fill=tk.X)
        
    def setup_color_tab(self, parent):
        """Setup color adjustments tab"""
        # Brightness
        ttk.Label(parent, text="Brightness:").pack(anchor=tk.W, pady=(0, 5))
        brightness_frame = ttk.Frame(parent)
        brightness_frame.pack(fill=tk.X, pady=(0, 10))
        
        self.brightness_var = tk.DoubleVar(value=1.0)
        ttk.Scale(brightness_frame, from_=0.1, to=3.0, variable=self.brightness_var,
                 orient=tk.HORIZONTAL).pack(side=tk.LEFT, fill=tk.X, expand=True)
        ttk.Label(brightness_frame, textvariable=self.brightness_var, width=6).pack(side=tk.RIGHT)
        ttk.Button(parent, text="Apply Brightness", command=self.apply_brightness).pack(fill=tk.X, pady=(0, 15))
        
        # Contrast
        ttk.Label(parent, text="Contrast:").pack(anchor=tk.W, pady=(0, 5))
        contrast_frame = ttk.Frame(parent)
        contrast_frame.pack(fill=tk.X, pady=(0, 10))
        
        self.contrast_var = tk.DoubleVar(value=1.0)
        ttk.Scale(contrast_frame, from_=0.1, to=3.0, variable=self.contrast_var,
                 orient=tk.HORIZONTAL).pack(side=tk.LEFT, fill=tk.X, expand=True)
        ttk.Label(contrast_frame, textvariable=self.contrast_var, width=6).pack(side=tk.RIGHT)
        ttk.Button(parent, text="Apply Contrast", command=self.apply_contrast).pack(fill=tk.X, pady=(0, 15))
        
        # Saturation
        ttk.Label(parent, text="Saturation:").pack(anchor=tk.W, pady=(0, 5))
        saturation_frame = ttk.Frame(parent)
        saturation_frame.pack(fill=tk.X, pady=(0, 10))
        
        self.saturation_var = tk.DoubleVar(value=1.0)
        ttk.Scale(saturation_frame, from_=0.0, to=3.0, variable=self.saturation_var,
                 orient=tk.HORIZONTAL).pack(side=tk.LEFT, fill=tk.X, expand=True)
        ttk.Label(saturation_frame, textvariable=self.saturation_var, width=6).pack(side=tk.RIGHT)
        ttk.Button(parent, text="Apply Saturation", command=self.apply_saturation).pack(fill=tk.X, pady=(0, 15))
        
        # Grayscale
        ttk.Button(parent, text="Convert to Grayscale", command=self.apply_grayscale).pack(fill=tk.X)
        
    def setup_effects_tab(self, parent):
        """Setup effects tab"""
        # Edge detection
        ttk.Label(parent, text="Edge Detection:").pack(anchor=tk.W, pady=(0, 10))
        
        ttk.Label(parent, text="Canny Threshold 1:").pack(anchor=tk.W)
        canny1_frame = ttk.Frame(parent)
        canny1_frame.pack(fill=tk.X, pady=(0, 5))
        self.canny1_var = tk.IntVar(value=100)
        ttk.Scale(canny1_frame, from_=0, to=300, variable=self.canny1_var,
                 orient=tk.HORIZONTAL).pack(side=tk.LEFT, fill=tk.X, expand=True)
        ttk.Label(canny1_frame, textvariable=self.canny1_var, width=5).pack(side=tk.RIGHT)
        
        ttk.Label(parent, text="Canny Threshold 2:").pack(anchor=tk.W)
        canny2_frame = ttk.Frame(parent)
        canny2_frame.pack(fill=tk.X, pady=(0, 10))
        self.canny2_var = tk.IntVar(value=200)
        ttk.Scale(canny2_frame, from_=0, to=300, variable=self.canny2_var,
                 orient=tk.HORIZONTAL).pack(side=tk.LEFT, fill=tk.X, expand=True)
        ttk.Label(canny2_frame, textvariable=self.canny2_var, width=5).pack(side=tk.RIGHT)
        
        ttk.Button(parent, text="Apply Canny Edge Detection", command=self.apply_canny).pack(fill=tk.X, pady=(0, 10))
        ttk.Button(parent, text="Apply Sobel Edge Detection", command=self.apply_sobel).pack(fill=tk.X, pady=(0, 15))
        
        # Thresholding
        ttk.Label(parent, text="Threshold Value:").pack(anchor=tk.W)
        threshold_frame = ttk.Frame(parent)
        threshold_frame.pack(fill=tk.X, pady=(0, 10))
        self.threshold_var = tk.IntVar(value=127)
        ttk.Scale(threshold_frame, from_=0, to=255, variable=self.threshold_var,
                 orient=tk.HORIZONTAL).pack(side=tk.LEFT, fill=tk.X, expand=True)
        ttk.Label(threshold_frame, textvariable=self.threshold_var, width=5).pack(side=tk.RIGHT)
        
        ttk.Button(parent, text="Apply Threshold", command=self.apply_threshold).pack(fill=tk.X, pady=(0, 5))
        ttk.Button(parent, text="Apply Otsu Threshold", command=self.apply_otsu).pack(fill=tk.X)
        
    def setup_transform_tab(self, parent):
        """Setup transformation tab"""
        # Rotation
        ttk.Label(parent, text="Rotation Angle:").pack(anchor=tk.W, pady=(0, 5))
        rotation_frame = ttk.Frame(parent)
        rotation_frame.pack(fill=tk.X, pady=(0, 10))
        
        self.rotation_var = tk.IntVar(value=0)
        ttk.Scale(rotation_frame, from_=-180, to=180, variable=self.rotation_var,
                 orient=tk.HORIZONTAL).pack(side=tk.LEFT, fill=tk.X, expand=True)
        ttk.Label(rotation_frame, textvariable=self.rotation_var, width=5).pack(side=tk.RIGHT)
        ttk.Button(parent, text="Apply Rotation", command=self.apply_rotation).pack(fill=tk.X, pady=(0, 15))
        
        # Flip
        ttk.Label(parent, text="Flip:").pack(anchor=tk.W, pady=(0, 5))
        ttk.Button(parent, text="Flip Horizontal", command=lambda: self.apply_flip('horizontal')).pack(fill=tk.X, pady=2)
        ttk.Button(parent, text="Flip Vertical", command=lambda: self.apply_flip('vertical')).pack(fill=tk.X, pady=2)
        ttk.Button(parent, text="Flip Both", command=lambda: self.apply_flip('both')).pack(fill=tk.X, pady=(2, 15))
        
        # Resize
        ttk.Label(parent, text="Resize Scale:").pack(anchor=tk.W, pady=(0, 5))
        resize_frame = ttk.Frame(parent)
        resize_frame.pack(fill=tk.X, pady=(0, 10))
        
        self.resize_var = tk.DoubleVar(value=1.0)
        ttk.Scale(resize_frame, from_=0.1, to=3.0, variable=self.resize_var,
                 orient=tk.HORIZONTAL).pack(side=tk.LEFT, fill=tk.X, expand=True)
        ttk.Label(resize_frame, textvariable=self.resize_var, width=6).pack(side=tk.RIGHT)
        ttk.Button(parent, text="Apply Resize", command=self.apply_resize).pack(fill=tk.X)
        
    def load_image(self):
        """Load an image from file"""
        file_path = filedialog.askopenfilename(
            title="Select an image",
            filetypes=[("Image files", "*.jpg *.jpeg *.png *.bmp *.tiff"), ("All files", "*.*")]
        )
        
        if file_path:
            try:
                self.processor = ImageProcessor(file_path)
                self.original_image = self.processor.image_cv.copy()
                self.current_image = self.original_image.copy()
                self.display_current_image()
                self.status_var.set(f"Loaded: {os.path.basename(file_path)}")
            except Exception as e:
                messagebox.showerror("Error", f"Failed to load image: {str(e)}")
                
    def save_image(self):
        """Save the current image"""
        if self.current_image is None:
            messagebox.showwarning("Warning", "No image to save")
            return
            
        file_path = filedialog.asksaveasfilename(
            defaultextension=".jpg",
            filetypes=[("JPEG", "*.jpg"), ("PNG", "*.png"), ("BMP", "*.bmp"), ("All files", "*.*")]
        )
        
        if file_path:
            try:
                cv2.imwrite(file_path, self.current_image)
                self.status_var.set(f"Saved: {os.path.basename(file_path)}")
                messagebox.showinfo("Success", "Image saved successfully!")
            except Exception as e:
                messagebox.showerror("Error", f"Failed to save image: {str(e)}")
                
    def reset_image(self):
        """Reset to original image"""
        if self.original_image is not None:
            self.current_image = self.original_image.copy()
            self.display_current_image()
            self.status_var.set("Reset to original image")
        else:
            messagebox.showwarning("Warning", "No image loaded")
            
    def display_current_image(self):
        """Display the current image on canvas"""
        if self.current_image is None:
            return
            
        # Convert BGR to RGB for display
        if len(self.current_image.shape) == 3:
            display_img = cv2.cvtColor(self.current_image, cv2.COLOR_BGR2RGB)
        else:
            display_img = self.current_image
            
        # Convert to PIL Image
        pil_img = Image.fromarray(display_img)
        
        # Resize to fit canvas
        canvas_width = self.canvas.winfo_width()
        canvas_height = self.canvas.winfo_height()
        
        if canvas_width > 1 and canvas_height > 1:
            img_width, img_height = pil_img.size
            scale = min(canvas_width / img_width, canvas_height / img_height, 1.0)
            new_width = int(img_width * scale)
            new_height = int(img_height * scale)
            
            pil_img = pil_img.resize((new_width, new_height), Image.Resampling.LANCZOS)
        
        # Convert to PhotoImage and display
        self.display_image = ImageTk.PhotoImage(pil_img)
        self.canvas.delete("all")
        self.canvas.create_image(canvas_width // 2, canvas_height // 2, 
                                image=self.display_image, anchor=tk.CENTER)
        
    def check_image_loaded(self):
        """Check if image is loaded"""
        if self.processor is None or self.current_image is None:
            messagebox.showwarning("Warning", "Please load an image first")
            return False
        return True
        
    # Filter operations
    def apply_blur(self):
        if not self.check_image_loaded():
            return
        kernel_size = self.blur_var.get()
        if kernel_size % 2 == 0:
            kernel_size += 1
        self.current_image = cv2.GaussianBlur(self.current_image, (kernel_size, kernel_size), 0)
        self.display_current_image()
        self.status_var.set(f"Applied Gaussian Blur (kernel: {kernel_size})")
        
    def apply_sharpen(self):
        if not self.check_image_loaded():
            return
        kernel = np.array([[-1, -1, -1], [-1, 9, -1], [-1, -1, -1]])
        self.current_image = cv2.filter2D(self.current_image, -1, kernel)
        self.display_current_image()
        self.status_var.set("Applied Sharpen filter")
        
    def apply_median_blur(self):
        if not self.check_image_loaded():
            return
        kernel_size = self.median_var.get()
        if kernel_size % 2 == 0:
            kernel_size += 1
        self.current_image = cv2.medianBlur(self.current_image, kernel_size)
        self.display_current_image()
        self.status_var.set(f"Applied Median Blur (kernel: {kernel_size})")
        
    # Color operations
    def apply_brightness(self):
        if not self.check_image_loaded():
            return
        pil_img = Image.fromarray(cv2.cvtColor(self.current_image, cv2.COLOR_BGR2RGB))
        enhancer = ImageEnhance.Brightness(pil_img)
        pil_img = enhancer.enhance(self.brightness_var.get())
        self.current_image = cv2.cvtColor(np.array(pil_img), cv2.COLOR_RGB2BGR)
        self.display_current_image()
        self.status_var.set(f"Applied Brightness: {self.brightness_var.get():.2f}")
        
    def apply_contrast(self):
        if not self.check_image_loaded():
            return
        pil_img = Image.fromarray(cv2.cvtColor(self.current_image, cv2.COLOR_BGR2RGB))
        enhancer = ImageEnhance.Contrast(pil_img)
        pil_img = enhancer.enhance(self.contrast_var.get())
        self.current_image = cv2.cvtColor(np.array(pil_img), cv2.COLOR_RGB2BGR)
        self.display_current_image()
        self.status_var.set(f"Applied Contrast: {self.contrast_var.get():.2f}")
        
    def apply_saturation(self):
        if not self.check_image_loaded():
            return
        pil_img = Image.fromarray(cv2.cvtColor(self.current_image, cv2.COLOR_BGR2RGB))
        enhancer = ImageEnhance.Color(pil_img)
        pil_img = enhancer.enhance(self.saturation_var.get())
        self.current_image = cv2.cvtColor(np.array(pil_img), cv2.COLOR_RGB2BGR)
        self.display_current_image()
        self.status_var.set(f"Applied Saturation: {self.saturation_var.get():.2f}")
        
    def apply_grayscale(self):
        if not self.check_image_loaded():
            return
        if len(self.current_image.shape) == 3:
            self.current_image = cv2.cvtColor(self.current_image, cv2.COLOR_BGR2GRAY)
        self.display_current_image()
        self.status_var.set("Converted to Grayscale")
        
    # Effects
    def apply_canny(self):
        if not self.check_image_loaded():
            return
        if len(self.current_image.shape) == 3:
            gray = cv2.cvtColor(self.current_image, cv2.COLOR_BGR2GRAY)
        else:
            gray = self.current_image
        self.current_image = cv2.Canny(gray, self.canny1_var.get(), self.canny2_var.get())
        self.display_current_image()
        self.status_var.set(f"Applied Canny Edge Detection ({self.canny1_var.get()}, {self.canny2_var.get()})")
        
    def apply_sobel(self):
        if not self.check_image_loaded():
            return
        if len(self.current_image.shape) == 3:
            gray = cv2.cvtColor(self.current_image, cv2.COLOR_BGR2GRAY)
        else:
            gray = self.current_image
        sobelx = cv2.Sobel(gray, cv2.CV_64F, 1, 0, ksize=5)
        sobely = cv2.Sobel(gray, cv2.CV_64F, 0, 1, ksize=5)
        sobel = np.sqrt(sobelx**2 + sobely**2)
        self.current_image = np.uint8(sobel)
        self.display_current_image()
        self.status_var.set("Applied Sobel Edge Detection")
        
    def apply_threshold(self):
        if not self.check_image_loaded():
            return
        if len(self.current_image.shape) == 3:
            gray = cv2.cvtColor(self.current_image, cv2.COLOR_BGR2GRAY)
        else:
            gray = self.current_image
        _, self.current_image = cv2.threshold(gray, self.threshold_var.get(), 255, cv2.THRESH_BINARY)
        self.display_current_image()
        self.status_var.set(f"Applied Binary Threshold: {self.threshold_var.get()}")
        
    def apply_otsu(self):
        if not self.check_image_loaded():
            return
        if len(self.current_image.shape) == 3:
            gray = cv2.cvtColor(self.current_image, cv2.COLOR_BGR2GRAY)
        else:
            gray = self.current_image
        _, self.current_image = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
        self.display_current_image()
        self.status_var.set("Applied Otsu Threshold")
        
    # Transformations
    def apply_rotation(self):
        if not self.check_image_loaded():
            return
        angle = self.rotation_var.get()
        height, width = self.current_image.shape[:2]
        center = (width // 2, height // 2)
        matrix = cv2.getRotationMatrix2D(center, angle, 1.0)
        self.current_image = cv2.warpAffine(self.current_image, matrix, (width, height))
        self.display_current_image()
        self.status_var.set(f"Rotated by {angle}°")
        
    def apply_flip(self, mode):
        if not self.check_image_loaded():
            return
        if mode == 'horizontal':
            self.current_image = cv2.flip(self.current_image, 1)
        elif mode == 'vertical':
            self.current_image = cv2.flip(self.current_image, 0)
        elif mode == 'both':
            self.current_image = cv2.flip(self.current_image, -1)
        self.display_current_image()
        self.status_var.set(f"Flipped {mode}")
        
    def apply_resize(self):
        if not self.check_image_loaded():
            return
        scale = self.resize_var.get()
        height, width = self.current_image.shape[:2]
        new_width = int(width * scale)
        new_height = int(height * scale)
        self.current_image = cv2.resize(self.current_image, (new_width, new_height), 
                                       interpolation=cv2.INTER_LINEAR)
        self.display_current_image()
        self.status_var.set(f"Resized to {scale:.2f}x scale ({new_width}x{new_height})")


if __name__ == "__main__":
    main()
