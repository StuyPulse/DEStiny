package edu.stuy.robot.commands;

import java.util.Arrays;

import edu.stuy.robot.Robot;
import edu.stuy.robot.cv.StuyVisionModule;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class CVReadAndPrintCommand extends Command {
    boolean tryToSaveFile;
    public CVReadAndPrintCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }
    public CVReadAndPrintCommand(boolean x) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        this.tryToSaveFile = x;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        try {
            long start = System.currentTimeMillis();
            double[] cvReading = null;
            if (tryToSaveFile) {
                cvReading = Robot.vision.processImageAndSave("/tmp/wb-img");
            } else {
                cvReading = Robot.vision.processImage();
            }
            System.out.println("\n\n\n\n\n\n\n\n\n\nprocessImage took " + (System.currentTimeMillis() - start) + "ms");
            System.out.println("Reading is: " + Arrays.toString(cvReading)); // Arrays.toString returns "null" is vec is null
            System.out.println("Distance is: " + StuyVisionModule.findDistanceToGoal(cvReading));
            if (cvReading != null) {
                System.out.println("Angle X is: " + StuyVisionModule.frameXPxToDegrees(cvReading[0]));
                System.out.println("Angle Y is: " + StuyVisionModule.frameYPxToDegrees(cvReading[1]));
                System.out.println("Y to horiz: " + StuyVisionModule.yInFrameToDegreesFromHorizon(cvReading[1]));
            }
            boolean canProceed = cvReading != null;
            double desiredAngle;
            SmartDashboard.putString("cv-reading", Arrays.toString(cvReading));
            if (canProceed) {
                desiredAngle = StuyVisionModule.frameXPxToDegrees(cvReading[0]);
                SmartDashboard.putNumber("cv-angle", desiredAngle);
                System.out.println("Desired Angle Delta: " + desiredAngle);
            }
            SmartDashboard.putBoolean("cv-visible", canProceed);
        } catch (Exception e) {
            System.err.println("\n\n\n\nGeneric exception caught in CVReadAndPrintCommand:");
            e.printStackTrace();
            System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
