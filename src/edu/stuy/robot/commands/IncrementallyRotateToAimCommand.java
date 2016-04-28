package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.stuy.robot.cv.StuyVision;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IncrementallyRotateToAimCommand extends Command {

    private double[] cvData;
    private double timeStart;

    private static final double timeout = 3.0;

    public IncrementallyRotateToAimCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        timeStart = Timer.getFPGATimestamp();
        cvData = Robot.vision.processImage();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (cvData == null) {
            return;
        }
        if (cvData[0] < 0.0) {
            Robot.drivetrain.tankDrive(-0.6, 0.6);
        } else if (cvData[0] > 0.0) {
            Robot.drivetrain.tankDrive(-0.6, 0.6);
        } else {
            Robot.drivetrain.tankDrive(0.0, 0.0);
        }
        cvData = Robot.vision.processImage();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Timer.getFPGATimestamp() - timeStart > timeout
                || Robot.oi.driverIsOverriding()
                || cvData == null
                || Math.abs(StuyVision.frameXPxToDegrees(cvData[0])) < 1;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.tankDrive(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
