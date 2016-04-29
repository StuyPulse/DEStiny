package edu.stuy.robot.commands;

import java.util.Arrays;

import edu.stuy.robot.Robot;
import edu.stuy.robot.cv.StuyVision;
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
    public CVReadAndPrintCommand(boolean save) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        tryToSaveFile = save;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        try {
            long start = System.currentTimeMillis();
            double[] cvReading = null;
            cvReading = Robot.vision.processImage(tryToSaveFile);
            System.out.println("\n\n\n\n\n\n\n\n\n\nprocessImage took " + (System.currentTimeMillis() - start) + "ms");
            System.out.println(new StuyVision.Report(cvReading));
            boolean canProceed = cvReading != null;
            SmartDashboard.putString("cv-reading", Arrays.toString(cvReading));
            if (canProceed) {
                double desiredAngle = StuyVision.frameXPxToDegrees(cvReading[0]);
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
