package edu.stuy.robot.cv;

import static edu.stuy.robot.RobotMap.CAMERA_FRAME_PX_HEIGHT;
import static edu.stuy.robot.RobotMap.CAMERA_FRAME_PX_WIDTH;
import static edu.stuy.robot.RobotMap.CAMERA_HEIGHT_FROM_GROUND;
import static edu.stuy.robot.RobotMap.CAMERA_TILT_ANGLE;
import static edu.stuy.robot.RobotMap.CAMERA_VIEWING_ANGLE_X;
import static edu.stuy.robot.RobotMap.CAMERA_VIEWING_ANGLE_Y;
import static edu.stuy.robot.RobotMap.HIGH_GOAL_HEIGHT;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import edu.stuy.robot.cv.gui.DoubleSV;
import edu.stuy.robot.cv.gui.IntegerSV;
import edu.stuy.robot.cv.gui.Main;
import edu.stuy.robot.cv.gui.VisionModule;
import edu.stuy.robot.cv.sources.CaptureSource;
import edu.stuy.robot.cv.sources.DeviceCaptureSource;

public class StuyVision extends VisionModule {

    // The following can be left in even in production, as the overhead
    // of using .value() is negligible
    public IntegerSV minH_GREEN = IntegerSV.mkColor(58, "Min Hue");
    public IntegerSV maxH_GREEN = IntegerSV.mkColor(123, "Max Hue");

    public IntegerSV minS_GREEN = IntegerSV.mkColor(104, "Min Saturation");
    public IntegerSV maxS_GREEN = IntegerSV.mkColor(255, "Max Saturation");

    public IntegerSV minV_GREEN = IntegerSV.mkColor(20, "Min Value");
    public IntegerSV maxV_GREEN = IntegerSV.mkColor(155, "Max Value");

    // Thresholds regarding the geometry of the bounding box of the region found
    // by the HSV filtering
    public DoubleSV minGoalArea = new DoubleSV(200.0, 0.0, 10000.0, "Min Goal Area");
    public DoubleSV maxGoalArea = new DoubleSV(30720.0, 0.0, 10000.0, "Max Goal Area");
    public DoubleSV minGoalRatio = new DoubleSV(1.1, 1.0, 10.0, "Min Goal Ratio");
    public DoubleSV maxGoalRatio = new DoubleSV(3.0, 1.0, 10.0, "Max Goal Ratio");

    private static final int outerUSBPort = 0;
    private int cameraPort;
    private CaptureSource camera;

    private static PrintWriter logWriter;

    public static void loadOpenCV() {
        // Load opencv native library
        String dir = StuyVision.class.getClassLoader().getResource("").getPath();
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            System.load(dir.substring(1).replaceAll("\\%20", " ")
                    + "..\\lib\\opencv-3.0.0\\build\\java\\x64\\opencv_java300.dll");
        } else {
            // This is the .so's location on the roboRio
            System.load("/usr/local/share/OpenCV/java/libopencv_java310.so");
        }
        try {
            logWriter = new PrintWriter("logs.txt");
        } catch (Exception e) {}
    }

    private void initializeCamera() {
        camera = new DeviceCaptureSource(cameraPort);
        System.out.println("Made camera");
    }

    public StuyVision() {
        try {
            cameraPort = outerUSBPort;
            initializeCamera();
        } catch (Exception e) {
            System.out.println("Failed to create camera at " + cameraPort + ". Error was: " + e);
        }
    }

    public StuyVision(CaptureSource camera) {
        cameraPort = outerUSBPort;
        this.camera = camera;
    }

    public StuyVision(int i) {
        try {
            cameraPort = i;
            initializeCamera();
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
        return (minGoalRatio.value() < ratio && ratio < maxGoalRatio.value())
                || (1 / maxGoalRatio.value() < ratio && ratio < 1 / minGoalRatio.value());
    }

    private double[] getLargestGoal(Mat originalFrame, Mat filteredImage, Main app) {
        boolean withGui = app != null;
        Mat drawn = null;
        if (withGui) {
            drawn = originalFrame.clone();
        }

        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(filteredImage, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        double largestArea = 0.0;
        RotatedRect largestRect = null;

        for (int i = 0; i < contours.size(); i++) {
            double currArea = Imgproc.contourArea(contours.get(i));
            if (currArea < minGoalArea.value() || currArea > maxGoalArea.value()) {
                continue;
            }
            MatOfPoint2f tmp = new MatOfPoint2f();
            contours.get(i).convertTo(tmp, CvType.CV_32FC1);
            RotatedRect r = Imgproc.minAreaRect(tmp);
            if (!aspectRatioThreshold(r.size.height, r.size.width)) {
                continue;
            }
            if (withGui) {
                Point[] points = new Point[4];
                r.points(points);
                for (int j = 0; j < points.length; j++) {
                    Imgproc.line(drawn, points[j], points[(j + 1) % 4], new Scalar(0, 255, 0));
                }
            }
            if (currArea > largestArea) {
                largestArea = currArea;
                largestRect = r;
            }
        }

        if (largestRect == null) {
            if (withGui) {
                // Post the unchanged image anyway for visual consistency
                app.postImage(originalFrame, "Goals", this);
            }
            // Return null to signify no goal found
            return null;
        }

        double[] vector = new double[3];
        vector[0] = largestRect.center.x - originalFrame.width() / 2.0;
        vector[1] = largestRect.center.y - originalFrame.height() / 2.0;
        vector[2] = largestRect.angle;

        if (withGui) {
            Imgproc.circle(drawn, largestRect.center, 1, new Scalar(0, 0, 255), 2);
            double w = drawn.width();
            double h = drawn.height();
            Imgproc.line(drawn,
                new Point(w / 2, h / 2),
                largestRect.center,
                new Scalar(0, 0, 255));
            app.postImage(drawn, "Goals", this);
        }

        return vector;
    }

    public double[] getLargestGoal(Mat orig, Mat f) {
        return getLargestGoal(orig, f, null);
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
    public double[] hsvThresholding(Mat frame, Main app) {
        boolean withGui = app != null;

        // Convert BGR camera image to HSV for processing
        Mat hsv = new Mat();
        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV);

        // Split HSV channels and process each channel
        ArrayList<Mat> greenFilterChannels = new ArrayList<Mat>();
        Core.split(hsv, greenFilterChannels);
        Core.inRange(greenFilterChannels.get(0), new Scalar(minH_GREEN.value()), new Scalar(maxH_GREEN.value()),
                greenFilterChannels.get(0));
        Core.inRange(greenFilterChannels.get(1), new Scalar(minS_GREEN.value()), new Scalar(maxS_GREEN.value()),
                greenFilterChannels.get(1));
        Core.inRange(greenFilterChannels.get(2), new Scalar(minV_GREEN.value()), new Scalar(maxV_GREEN.value()),
                greenFilterChannels.get(2));

        // Merge filtered H, S and V back into one binarized image
        Mat greenFiltered = new Mat();
        Core.bitwise_and(greenFilterChannels.get(0), greenFilterChannels.get(1), greenFiltered);
        Core.bitwise_and(greenFilterChannels.get(2), greenFiltered, greenFiltered);
        if (withGui) {
            app.postImage(greenFiltered, "After filtering H, S, V", this);
        }

        // Erode and dilate to remove noise
        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9, 9));
        Imgproc.erode(greenFiltered, greenFiltered, erodeKernel);
        Imgproc.dilate(greenFiltered, greenFiltered, dilateKernel);
        if (withGui) {
            app.postImage(greenFiltered, "After erode/dilate", this);
        }

        double[] output = getLargestGoal(frame, greenFiltered, app);
        try {
            logWriter.println("Vector calculated: " + Arrays.toString(output));
            logWriter.flush();
        } catch (Exception e) {
        }
        return output;
    }

    public double[] hsvThresholding(Mat frame) {
        return hsvThresholding(frame, null);
    }

    public double[] processImage() {
        if (camera == null) {
            System.out.println("Camera object is uninitialized!");
            return null;
        }
        Mat frame = camera.read();
        System.out.println("Got frame from camera");
        if (frame == null) {
            System.out.println("FRAME WAS NULL");
            return null;
        }
        return hsvThresholding(frame);
    }

    public double[] processImageAndSave(String path) {
        if (camera == null) {
            System.out.println("Camera object is uninitialized!");
            return null;
        }
        Mat frame = camera.read();
        if (frame == null) {
            System.out.println("FRAME WAS NULL");
            return null;
        }
        double[] result = hsvThresholding(frame);
        try{
            Imgcodecs.imwrite(path, frame);
        }catch(Exception e) {e.printStackTrace();}
        return result;
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

    public static double frameXPxToDegrees(double px) {
        return CAMERA_VIEWING_ANGLE_X * px / CAMERA_FRAME_PX_WIDTH;
    }

    public static double frameYPxToDegrees(double dy) {
        return dy / CAMERA_FRAME_PX_HEIGHT * CAMERA_VIEWING_ANGLE_Y;
    }

    public static double yInFrameToDegreesFromHorizon(double height) {
        return CAMERA_TILT_ANGLE - frameYPxToDegrees(height);
    }

    public static double findDistanceToGoal(double frameY) {
        double angle = yInFrameToDegreesFromHorizon(frameY);
        return (HIGH_GOAL_HEIGHT - CAMERA_HEIGHT_FROM_GROUND) / Math.tan(Math.toRadians(angle));
    }

    public static double findDistanceToGoal(double[] vec) {
        if (vec == null) {
            return -1;
        }
        return findDistanceToGoal(vec[1]);
    }

    private static class Report {
        double[] reading;
        double goalDegsY;
        double goalDegsX;
        double inchesAway;
        public Report(double[] visionReading) {
            reading = visionReading;
            goalDegsX = frameXPxToDegrees(reading[0]);
            goalDegsY = yInFrameToDegreesFromHorizon(reading[1]);
            inchesAway = findDistanceToGoal(reading[1]);
            // let null pointer exception occur if data is null
        }
        public String toString() {
            return "CV Read.\n"
                + " Raw: " + Arrays.toString(reading) + "\n"
                + " Goal Degs X: " + goalDegsX + "\n"
                + " Goal Degs Y: " + goalDegsY + "\n"
                + " Distance (in): " + inchesAway + "\n";
        }
    }

    public static void main(String[] args) {
        System.out.println("Running test: read from frame and determine angle to rotate");
        StuyVision sv = new StuyVision();
        Report r = new Report(sv.processImage());
        System.out.println(r);
    }

    public void run(Main app, Mat frame) {
        app.postImage(frame, "Video", this);
        double[] result = hsvThresholding(frame, app);
        System.out.println(Arrays.toString(result));
    }
}
