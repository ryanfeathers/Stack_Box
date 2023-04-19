# Stack_Box
ImageJ plugin for cropping multichannel z-stacks at user specified points to a specific box size. Boxed images are saved individually with the channel,z frame, and coordinates appended to the filename. Useful for creating cropped images of individual cells from confocal fluorescence microscopy images. 

Copy the JAR file to the plugins directory in ImageJ/FIJI. Tested with version 2.9.0/1.53t

1) Open an image and add points with the multi-point tool
2) Open the ROI manager and add the points: Analyze->tools->ROI manager ->add
3) Run the Stack_Box plugin and provide a box size when prompted
4) Boxed images for each frame and channel will be saved in the folder where the original file is located

Note: Adjusting or normalizing brightness and contrast and clicking "Apply" crops the intensity range. The current version saves raw files without information loss. When opening the cropped images in ImageJ/FIJI you will need to specify the bit depth and the image size (specfied box size). The bit depth can be found in the original image header.

![alt text](https://github.com/ryanfeathers/Stack_Box/blob/main/Stackboxpluginmenu.png?raw=true)


![alt text](https://github.com/ryanfeathers/Stack_Box/blob/main/stackbox_output2.png?raw=true)


![alt text](https://github.com/ryanfeathers/Stack_Box/blob/main/stackbox_output1.png?raw=true)


