package edu.stuy.robot.commands;

import java.util.Arrays;

import edu.stuy.robot.Robot;
import edu.stuy.robot.cv.StuyVisionModule;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateToAimCommand extends GyroRotationalCommand {

    public RotateToAimCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super();
    }

    private double[] cvReading;

    protected void setDesiredAngle() {
        long start = System.currentTimeMillis();
        cvReading = Robot.vision.processImage();
        System.out.println("Image processing took " + (System.currentTimeMillis() - start) + "ms");
        canProceed = cvReading != null;
        SmartDashboard.putString("cv-reading", Arrays.toString(cvReading));
        if (canProceed) {
            desiredAngle = StuyVisionModule.frameXPxToDegrees(cvReading[0]);
            SmartDashboard.putNumber("cv-angle", desiredAngle);
            System.out.println("Reading was: " + Arrays.toString(cvReading) + "-----------------------------");
            System.out.println("Desired Angle Delta: " + desiredAngle);
        } else {
            System.out.println("Reading was NULL------------------------------------------------------------");
        }
        SmartDashboard.putBoolean("cv-visible", canProceed);
    }
}
