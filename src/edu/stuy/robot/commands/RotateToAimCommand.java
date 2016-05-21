package edu.stuy.robot.commands;

import java.util.Arrays;

import edu.stuy.robot.Robot;
import edu.stuy.robot.cv.StuyVision;
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
        cvReading = Robot.vision.processImage();
        canProceed = cvReading != null;
        SmartDashboard.putString("cv-reading", Arrays.toString(cvReading));
        if (canProceed) {
            desiredAngle = StuyVision.frameXPxToDegrees(cvReading[0]);
            SmartDashboard.putNumber("cv-angle", desiredAngle);
        }
        SmartDashboard.putBoolean("cv-visible", canProceed);
    }

    protected void onEnd() {
        System.out.println(new StuyVision.Report(cvReading));
    }
}
