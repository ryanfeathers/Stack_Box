# Stack_Box
ImageJ plugin for cropping multichannel z-stacks at user specified points to a specified box size. Boxed images are saved individually with the channel,z frame, and coordinates appended to the filename.

Copy the JAR file to the plugins directory in ImageJ/FIJI. Tested with version 2.9.0/1.53t

1) Open an image and add points with the multi-point tool
2) Open the ROI manager and add the points: Analyze->tools->ROI manager ->add
3) Run the Stack_Box plugin and provide a box size when prompted
4) Boxed images for each frame and channel will be saved in the folder where the original file is located

Note: Adjust or normalize brightness and contrast before cropping.
