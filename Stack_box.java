import ij.*;
import ij.gui.*;
import ij.plugin.*;
import ij.process.*;
import ij.io.*;
import ij.plugin.frame.RoiManager;
import java.awt.*;
import java.util.*;

public class Stack_box implements PlugIn {
    private ImagePlus img;
    private int boxSize;

    public void run(String arg) {
        img = WindowManager.getCurrentImage();
        if (img == null) {
            IJ.error("No image is open.");
            return;
        }

        RoiManager roiManager = RoiManager.getInstance();
        if (roiManager == null || roiManager.getCount() == 0) {
            IJ.error("No points selected. Please use the Multi-point tool to select points.");
            return;
        }

        GenericDialog gd = new GenericDialog("Box Size");
        gd.addNumericField("Enter box size:", 50, 0); // default box size: 50
        gd.showDialog();
        if (gd.wasCanceled()) {
            return;
        }

        boxSize = (int) gd.getNextNumber();
        processImage(roiManager);
    }

    private void processImage(RoiManager roiManager) {
        int numSlices = img.getNSlices();
        int numChannels = img.getNChannels();

        for (int i = 0; i < roiManager.getCount(); i++) {
            PointRoi pointRoi = (PointRoi) roiManager.getRoi(i);
            Point[] points = pointRoi.getContainedPoints();
            for (Point point : points) {
                for (int slice = 1; slice <= numSlices; slice++) {
                    for (int channel = 1; channel <= numChannels; channel++) {
                        img.setPosition(channel, slice, 1);
                        ImagePlus croppedImage = cropImage(img, point, boxSize);
                        String outputFileName = generateOutputFileName(img.getTitle(), point, channel, slice);
                        saveCroppedImage(croppedImage, outputFileName);
                    }
                }
            }
        }
    }

    private ImagePlus cropImage(ImagePlus img, Point point, int boxSize) {
        int x = (int) point.getX() - boxSize / 2;
        int y = (int) point.getY() - boxSize / 2;
        int width = boxSize;
        int height = boxSize;
        Roi roi = new Roi(x, y, width, height);
        img.setRoi(roi);
        return img.crop();
    }

    private String generateOutputFileName(String originalFileName, Point point, int channel, int slice) {
        return originalFileName + "_x" + point.x + "_y" + point.y + "_c" + channel + "_z" + slice + ".raw";
    }

    private void saveCroppedImage(ImagePlus croppedImage, String outputFileName) {
        FileSaver fileSaver = new FileSaver(croppedImage);
        String outputPath = IJ.getDirectory("image") + outputFileName;
        fileSaver.saveAsRaw(outputPath);
    }
}
