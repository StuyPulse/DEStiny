package edu.stuy.robot.cv;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.stuy.robot.cv.sources.CaptureSource;
import edu.stuy.robot.cv.sources.DeviceCaptureSource;

public class StuyVisionModule {

    public int minH_GREEN = 58;
    public int maxH_GREEN = 123;

    public int minS_GREEN = 104;
    public int maxS_GREEN = 255;

    public int minV_GREEN = 20;
    public int maxV_GREEN = 155;

    // Thresholds regarding the geometry of the bounding box of the region found
    // by the HSV filtering
    public double minAreaThreshold = 200.0;
    public double maxAreaThreshold = 30720.0;
    public double minRatioThreshold = 1.1;
    public double maxRatioThreshold = 3.0;

    private static final int outerUSBPort = 0;
    private int cameraPort;
    private CaptureSource camera;

    private static PrintWriter logWriter;

    static {
        // Load opencv native library
        String dir = StuyVisionModule.class.getClassLoader().getResource("").getPath();
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            System.load(dir + "..\\lib\\opencv-3.0.0\\build\\lib\\opencv_java300.dll");
        } else {
            // This is the .so's location on the roboRio
            System.load("/usr/local/share/OpenCV/java/libopencv_java310.so");
        }
        try {
            logWriter = new PrintWriter("logs.txt");
        } catch (Exception e) {
        }
    }

    public StuyVisionModule() {
        try {
            cameraPort = outerUSBPort;
            camera = new DeviceCaptureSource(cameraPort);
            System.out.println("Made camera");
        } catch (Exception e) {
            System.out.println("Failed to create camera at " + cameraPort + ". Error was: " + e);
        }
    }

    public StuyVisionModule(CaptureSource camera) {
        cameraPort = outerUSBPort;
        this.camera = camera;
    }

    public StuyVisionModule(int i) {
        try {
            cameraPort = i;
            camera = new DeviceCaptureSource(i);
            System.out.println("Made camera");
        } catch (Exception e) {
            System.out.println("Failed to create camera at " + i + ", will reattempt later. Error was: " + e);
        }
    }

    /**
     * Given the dimensions of a rectangle, return whether the ratio of these
     * rectangle's dimensions suggests it may be a valid goal.
     * 
     * @param height
     * @param width
     * @return
     */
    private boolean aspectRatioThreshold(double height, double width) {
        double ratio = width / height;
        return (minRatioThreshold < ratio && ratio < maxRatioThreshold)
                || (1 / maxRatioThreshold < ratio && ratio < 1 / minRatioThreshold);
    }

    private double[] getLargestGoal(Mat originalFrame, Mat filteredImage) {
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(filteredImage, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        double largestArea = 0.0;
        RotatedRect largestRect = null;

        for (int i = 0; i < contours.size(); i++) {
            double currArea = Imgproc.contourArea(contours.get(i));
            if (currArea < minAreaThreshold || currArea > maxAreaThreshold) {
                continue;
            }
            MatOfPoint2f tmp = new MatOfPoint2f();
            contours.get(i).convertTo(tmp, CvType.CV_32FC1);
            RotatedRect r = Imgproc.minAreaRect(tmp);
            if (!aspectRatioThreshold(r.size.height, r.size.width)) {
                continue;
            }
            if (currArea > largestArea) {
                largestArea = currArea;
                largestRect = r;
            }
        }

        if (largestRect == null) {
            // Return null to signify no goal found
            return null;
        }

        double[] vector = new double[3];
        vector[0] = largestRect.center.x - originalFrame.width() / 2.0;
        vector[1] = largestRect.center.y - originalFrame.height() / 2.0;
        vector[2] = largestRect.angle;

        return vector;
    }

    /**
     * Process an image to look for a goal, and, if a <code>app</code> is
     * passed, post two intermediate states of the image from during processing
     * to the gui
     * 
     * @param frame
     *     The image to process
     * 
     * @param app
     *     (Optional: pass <code>null</code> to ignore) The
     *     <code>Main</code> to post intermediate states of the processed
     *     image to.
     * 
     * @return
     *     Three doubles, in a <code>double[3]</code>, ordered as such:
     *     <p>
     *         <code>index 0</code>: The x-offset, in pixels, of the center of
     *         the bounding rectangle of the found goal from the center of the
     *         image
     *     </p>
     *     <p>
     *         <code>index 1</code>: The y-offset, in pixels, of the center of
     *         the bounding rectangle of the found goal form the center of the
     *         image
     *     </p>
     *     <p>
     *         <code>index 2</code>: The angle at which the bounding rectangle
     *         is tilted
     *     </p>
     */
    public double[] hsvThresholding(Mat frame) {
        // Convert BGR camera image to HSV for processing
        Mat hsv = new Mat();
        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV);

        // Split HSV channels and process each channel
        ArrayList<Mat> greenFilterChannels = new ArrayList<Mat>();
        Core.split(hsv, greenFilterChannels);
        Core.inRange(greenFilterChannels.get(0), new Scalar(minH_GREEN), new Scalar(maxH_GREEN),
                greenFilterChannels.get(0));
        Core.inRange(greenFilterChannels.get(1), new Scalar(minS_GREEN), new Scalar(maxS_GREEN),
                greenFilterChannels.get(1));
        Core.inRange(greenFilterChannels.get(2), new Scalar(minV_GREEN), new Scalar(maxV_GREEN),
                greenFilterChannels.get(2));

        // Merge filtered H, S and V back into one binarized image
        Mat greenFiltered = new Mat();
        Core.bitwise_and(greenFilterChannels.get(0), greenFilterChannels.get(1), greenFiltered);
        Core.bitwise_and(greenFilterChannels.get(2), greenFiltered, greenFiltered);

        // Erode and dilate to remove noise
        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9, 9));
        Imgproc.erode(greenFiltered, greenFiltered, erodeKernel);
        Imgproc.dilate(greenFiltered, greenFiltered, dilateKernel);

        double[] output = getLargestGoal(frame, greenFiltered);
        try {
            logWriter.println("Vector calculated: " + Arrays.toString(output));
            logWriter.flush();
        } catch (Exception e) {
        }
        return output;
    }

    public double[] processImage() {
        if (camera == null) {
            System.out.println("Camera object is uninitialized!");
            return null;
        }
        Mat frame = camera.read();
        if (frame == null) {
            System.out.println("FRAME WAS NULL");
            return null;
        }
        return hsvThresholding(frame);
    }

    /**
     * Tests time taken to process <code>iters</code> frames read from
     * <code>cs</code>
     * 
     * @param cs
     *     The CaptureSource from which to read frames
     * @param iters
     *     The number of frames to read from <code>cs</code> and to
     *     process and time
     * @return The average time taken by <code>hsvThresholding</code> to process
     *     one of the frames
     */
    public double testProcessingTime(CaptureSource cs, int iters) {
        int total = 0;
        double[] vec;
        for (int i = 0; i < iters; i++) {
            long start = System.currentTimeMillis();
            vec = processImage();
            total += (int) (System.currentTimeMillis() - start);
        }
        return total / (double) iters;
    }
}
