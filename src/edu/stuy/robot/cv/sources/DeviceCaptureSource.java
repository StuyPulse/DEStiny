package edu.stuy.robot.cv.sources;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
public class DeviceCaptureSource extends CaptureSource {

    private final int deviceNo;
    private VideoCapture capture = null;

    public DeviceCaptureSource(int device) {
        this.deviceNo = device;
        reinitializeCaptureSource();
    }

    public DeviceCaptureSource(int device, int maxDimension) {
        this(device);
        setMaxImageDimension(maxDimension);
    }

    @Override
    public void reinitializeCaptureSource() {
        if (capture != null) {
            capture.release();
        }
        capture = new VideoCapture(deviceNo);
    }

    @Override
    public boolean isOpened() {
        return capture.isOpened();
    }

    @Override
    public boolean readFrame(Mat mat) {
        return capture.read(mat);
    }

    public void setExposure(int value) {
        capture.set(15, value);
    }

    public void setBrightness(int value) {
        capture.set(10, value);
    }
}
