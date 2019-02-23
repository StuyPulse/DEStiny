package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DrivetrainTankDriveCommand extends Command {

    public DrivetrainTankDriveCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.overrideAutoGearShifting = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double left = Robot.drivetrain.inputSquared(Robot.oi.driverGamepad.getLeftY());
        double right = Robot.drivetrain.inputSquared(Robot.oi.driverGamepad.getRightY());
        Robot.drivetrain.tankDrive(-left, -right);

        if (Math.abs(left) > 0.2 || Math.abs(right) > 0.2) {
            // Turn off cv signal light and stop hopper light blinking, as the bot is no longer auto-aligned
            Robot.cvSignalLight.stayOff();
            Robot.blueSignalLight.setBlinking(false);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
