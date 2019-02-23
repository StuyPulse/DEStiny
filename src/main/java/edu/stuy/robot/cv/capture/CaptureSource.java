package edu.stuy.robot.cv.capture;

import static edu.stuy.robot.RobotMap.CAMERA_FRAME_PX_WIDTH;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.stuy.robot.cv.util.DebugPrinter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class CaptureSource {

    private int resizeWidth = CAMERA_FRAME_PX_WIDTH;

    public abstract void reinitializeCaptureSource();

    public void setMaxImageDimension(int dim) {
        resizeWidth = dim;
    }

    public abstract boolean isOpened();

    public Mat readSized() {
        return readSized(new Mat(), new Mat());
    }

    public Mat readSized(Mat frame, Mat resizedFrame) {
        boolean success = readFrame(frame);
        if (success) {
            int frameHeight = frame.height();
            int frameWidth = frame.width();
            double resizeRatio = (double) resizeWidth / frameWidth;
            Size desiredSize = new Size(frameWidth * resizeRatio, frameHeight * resizeRatio);
            Imgproc.resize(frame, resizedFrame, desiredSize, 0, 0, Imgproc.INTER_CUBIC);
            DebugPrinter.println("\nCaptureSource: init frame dims: (" + frameWidth + ", " + frameHeight + ").");
            DebugPrinter.println("Ratio: " + resizeRatio + ".");
            DebugPrinter.println("New dims: (" + resizedFrame.width() + ", " + resizedFrame.height() + ")\n");
            try {
                SmartDashboard.putNumber("Camera init frame width", frameWidth);
                SmartDashboard.putNumber("Camera init frame height", frameHeight);
                SmartDashboard.putNumber("Camera frame width", resizedFrame.width());
                SmartDashboard.putNumber("Camera frame height", resizedFrame.height());
                SmartDashboard.putBoolean("Camera isOpened", isOpened());
            } catch (Error e) {
                // When testing off-robot the above will obviously fail
            }
            return resizedFrame;
        } else {
            return null;
        }
    }

    public abstract boolean readFrame(Mat mat);
}
