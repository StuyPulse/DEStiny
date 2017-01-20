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
        super(Robot.stopAutoMovement, false);
    }

    public RotateToAimCommand(boolean gentle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super(Robot.stopAutoMovement, gentle);
    }

    public RotateToAimCommand(boolean gentle, double tolerance) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super(Robot.stopAutoMovement, gentle, tolerance);
    }

    private double[] cvReading;

    @Override
    protected double setDesiredAngle() {
    	// Process and image and save result to cvReading
    	cvReading = Robot.vision.processImage();

    	// Robot.cvFoundGoal is used in autonomous
        Robot.cvFoundGoal = cvReading != null;

        // Update SmartDashboard
        SmartDashboard.putString("cv-reading", Arrays.toString(cvReading));
        SmartDashboard.putBoolean("cv-visible", Robot.cvFoundGoal);

        // Return the number of degrees to rotate if we found the goal, otherwise NaN
        double angleToRotate;
    	if (Robot.cvFoundGoal) {
            angleToRotate = StuyVision.frameXPxToDegrees(cvReading[0]);
            SmartDashboard.putNumber("cv-angle", angleToRotate);
        } else {
        	// NaN indicates to GyroRotationalCommand that we should cancel the rotation
        	angleToRotate = Double.NaN;
        }
    	return angleToRotate;
    }

    @Override
    protected void onEnd() {
        System.out.println(new StuyVision.Report(cvReading));
    }
}
