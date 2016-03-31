package edu.stuy.robot.cv.sources;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import edu.stuy.robot.cv.util.FileManager;

public class ImageCaptureSource extends CaptureSource {

    private final String filename;
    private Mat mat = null;

    public ImageCaptureSource(String filename) {
        FileManager.assertFileExists(filename);
        this.filename = filename;
        reinitializeCaptureSource();
    }

    public ImageCaptureSource(String filename, int maxDimension) {
        this(filename);
        setMaxImageDimension(maxDimension);
    }

    @Override
    public void reinitializeCaptureSource() {
        mat = Imgcodecs.imread(filename);
    }

    @Override
    public boolean isOpened() {
        return mat != null;
    }

    @Override
    public boolean readFrame(Mat mat) {
        this.mat.copyTo(mat);
        return true;
    }

}
