package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DrivetrainDriveStraightCommand extends Command {

    private double distance;
    private double speed;
    private double leftDist;
    private double rightDist;

    public DrivetrainDriveStraightCommand(double _distance, double _speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        distance = _distance;
        speed = _speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.resetEncoders();
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        leftDist = Robot.drivetrain.getLeftEncoder();
        rightDist = Robot.drivetrain.getRightEncoder();
        if (encodersBroken()) {
            return;
        }
        if (leftDist < 0 || rightDist < 0) {
            Robot.drivetrain.tankDrive(speed, speed);
        } else if (leftDist - rightDist > 3.0f) {
            Robot.drivetrain.tankDrive(speed * getSpeedMultiplier(), speed);
        } else if (rightDist - leftDist > 3.0f) {
            Robot.drivetrain.tankDrive(speed, speed * getSpeedMultiplier());
        } else {
            Robot.drivetrain.tankDrive(speed, speed);
        }
    }

    private boolean encodersBroken() {
        return leftDist < 0 && rightDist < 0;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return encodersBroken() || Robot.drivetrain.getDistance() >= distance;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

    /**
     * @return The factor at which side you slow down if you are moving too fast
     */
    private double getSpeedMultiplier() {
        if (leftDist > rightDist) {
            return 1.0 - Math.min(((leftDist - rightDist) / (rightDist + 0.5)), 1.0);
        } else if (rightDist > leftDist) {
            return 1.0 - Math.min(((rightDist - leftDist) / (leftDist + 0.5)), 1.0);
        } else {
            return 1.0;
        }
    }
}
