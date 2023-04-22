import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.gui.Roi;
import ij.plugin.PlugIn;
import ij.plugin.frame.RoiManager;
import ij.gui.PointRoi;
import ij.io.FileSaver;
import java.awt.*;
import java.io.File;
import ij.plugin.tool.PlugInTool;
import ij.gui.PointRoi;

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
        if (roiManager == null) {
            roiManager = new RoiManager();
        }

        Roi roi = img.getRoi();
        if (roi != null && roi.getType() == Roi.POINT) {
            PointRoi pointRoi = (PointRoi) roi;
            roiManager.addRoi(pointRoi);
        } else {
            IJ.error("No points selected. Please use the Multi-point tool to select points.");
            return;
        }

        GenericDialog gd = new GenericDialog("Box Size");
        gd.addNumericField("Enter box size:", 200, 0); // default box size: 50
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
        String fileNameWithoutExtension = originalFileName.lastIndexOf(".") > 0
                ? originalFileName.substring(0, originalFileName.lastIndexOf("."))
                : originalFileName;
        return fileNameWithoutExtension + "_x" + point.x + "_y" + point.y + "_c" + channel + "_z" + slice + ".tif";
    }

    private void saveCroppedImage(ImagePlus croppedImage, String outputFileName) {
        String outputDirectoryPath = IJ.getDirectory("image") + "Cropped_Images" + File.separator;
        File outputDirectory = new File(outputDirectoryPath);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }

        FileSaver fileSaver = new FileSaver(croppedImage);
        fileSaver.saveAsTiff(outputDirectoryPath + outputFileName);
    }

}

