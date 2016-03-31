package edu.stuy.robot.cv.sources;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public abstract class CaptureSource {

    private int maxImageDimension = 360;

    public abstract void reinitializeCaptureSource();

    public void setMaxImageDimension(int dim) {
        this.maxImageDimension = dim;
    }

    public abstract boolean isOpened();

    public Mat read() {
        Mat frame = new Mat();
        boolean success = readFrame(frame);
        if (success) {
            int frameHeight = frame.height();
            int frameWidth = frame.width();
            double resizeRatio = (double) maxImageDimension / Math.max(frameHeight, frameWidth);
            Size desiredSize = new Size(frameWidth * resizeRatio, frameHeight * resizeRatio);
            Mat resizedFrame = new Mat();
            Imgproc.resize(frame, resizedFrame, desiredSize, 0, 0, Imgproc.INTER_CUBIC);
            return resizedFrame;
        }
        else {
            return null;
        }
    }

    public abstract boolean readFrame(Mat mat);
}
