package edu.stuy.robot.cv.sources;

import static edu.stuy.robot.RobotMap.CAMERA_FRAME_PX_WIDTH;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public abstract class CaptureSource {

    private int width = CAMERA_FRAME_PX_WIDTH;

    public abstract void reinitializeCaptureSource();

    public void setMaxImageDimension(int dim) {
        this.width = dim;
    }

    public abstract boolean isOpened();

    public Mat read() {
        Mat frame = new Mat();
        boolean success = readFrame(frame);
        if (success) {
            int frameHeight = frame.height();
            int frameWidth = frame.width();
            double resizeRatio = (double) CAMERA_FRAME_PX_WIDTH / frameWidth;
            Size desiredSize = new Size(frameWidth * resizeRatio, frameHeight * resizeRatio);
            Mat resizedFrame = new Mat();
            Imgproc.resize(frame, resizedFrame, desiredSize, 0, 0, Imgproc.INTER_CUBIC);
            System.out.println("\n\n\n\nCS| init frame dims: (" + frameWidth + ", " + frameHeight + ").\nRatio: " + resizeRatio + ".\nNew dims: (" + resizedFrame.width() + ", " + resizedFrame.height() + ")\n\n\n");
            return resizedFrame;
        } else {
            return null;
        }
    }

    public abstract boolean readFrame(Mat mat);
}
