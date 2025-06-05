import os
from torchvision import datasets, transforms
from torch.utils.data import random_split, DataLoader

# Transformações das imagens
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
])

# Carregar dataset
dataset_path = '4periodo/IA/Lista11/PetImages'
dataset = datasets.ImageFolder(root=dataset_path, transform=transform)

# Calcular tamanhos
dataset_size = len(dataset)
train_size = int(0.7 * dataset_size)
val_size = int(0.15 * dataset_size)
test_size = dataset_size - train_size - val_size

# Dividir o dataset
train_ds, val_ds, test_ds = random_split(dataset, [train_size, val_size, test_size])

# Criar DataLoaders
train_loader = DataLoader(train_ds, batch_size=32, shuffle=True)
val_loader = DataLoader(val_ds, batch_size=32, shuffle=False)
test_loader = DataLoader(test_ds, batch_size=32, shuffle=False)

print(f"Train: {train_size} imagens")
print(f"Validation: {val_size} imagens")
print(f"Test: {test_size} imagens")

