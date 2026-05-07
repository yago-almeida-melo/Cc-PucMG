# Mammogram Density Classifier (PAI)

GUI app in Python to read mammograms, segment breast region, and classify BI-RADS density using DenseNet121 or InceptionV3.

## Features
- Load PNG/TIFF images with any grayscale bit depth (8-16 bits)
- Zoom and visualize images
- Automatic breast segmentation and masking
- Train/test split based on filename index (multiples of 4 to test)
- Data augmentation with rotations: -20, -10, 0, 10, 20 degrees
- Binary classification (I+II vs III+IV) with sensitivity/specificity/precision/accuracy/F1
- 4-class classification with confusion matrix and average sensitivity/specificity
- Grad-CAM visualization on a selected image

## Requirements
- Python 3.9+
- numpy
- opencv-python
- pillow
- torch
- torchvision
- tkinter (standard library)

Example install:
```bash
pip install numpy opencv-python pillow torch torchvision
```

## Dataset naming rules
- BI-RADS class is inferred from filename letter:
  - D -> BIRADS I
  - E -> BIRADS II
  - F -> BIRADS III
  - G -> BIRADS IV
- Train/test split uses the last number in the filename:
  - if (number % 4 == 0) -> test
  - otherwise -> train

Example:
- `D_0012.png` goes to test
- `F_0013.tif` goes to train

## How to run
```bash
python main.py
```

## GUI flow
1. Load Image: open a single PNG/TIFF.
2. Segment: apply automatic segmentation to remove background/annotations.
3. Dataset Dir: select the directory containing all images.
4. Choose Model (DenseNet121 or InceptionV3).
5. Choose Mode (binary or 4class).
6. Train: retrains the final classifier layer.
7. Evaluate: runs on the test split and prints metrics in the log.
8. Save Weights / Load Weights: persist trained models.
9. Classify Current: runs prediction for the loaded image.
10. Grad-CAM: overlays the attention map for the decision.

## Notes
- The app uses pretrained weights from torchvision when available and retrains only the final layer.
- GPU is used automatically when available.
- The segmentation step uses Otsu thresholding + morphological cleanup + largest component.

## Files
- main.py: full implementation
- README.md: this file
