package edu.stuy.robot.cv.sources;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import edu.stuy.robot.cv.util.FileManager;

public class VideoCaptureSource extends CaptureSource {

    private final String filename;
    private VideoCapture capture = null;

    public VideoCaptureSource(String filename) {
        FileManager.assertFileExists(filename);
        this.filename = filename;
        reinitializeCaptureSource();
    }

    public VideoCaptureSource(String filename, int maxDimension) {
        this(filename);
        setMaxImageDimension(maxDimension);
    }

    @Override
    public void reinitializeCaptureSource() {
        if (capture != null) {
            capture.release();
        }
        capture = new VideoCapture(filename);
    }

    @Override
    public boolean isOpened() {
        return capture.isOpened();
    }

    @Override
    public boolean readFrame(Mat mat) {
        return capture.read(mat);
    }

}
