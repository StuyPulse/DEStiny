package edu.stuy.robot.cv.gui;

import org.opencv.core.Mat;

public abstract class VisionModule {

    public abstract void run(Main app, Mat frame);

    public String getName() {
        return getClass().getSimpleName();
    }
}
