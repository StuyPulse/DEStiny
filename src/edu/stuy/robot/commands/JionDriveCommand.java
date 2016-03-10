package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * JionDrive - When the left trigger is held, the drivetrain is set to high
 * gear. When it's let go, go back to automatic gear shifting, unless the
 * drivetrain is overridden.
 */
public class JionDriveCommand extends Command {

    public JionDriveCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.overrideAutoGearShifting = true; // Disable automatic
                                                          // gear shift
        Robot.drivetrain.manualGearShift(true); // Shift to high gear
        double left = Robot.drivetrain.inputSquared(Robot.oi.driverGamepad.getLeftY());
        double right = Robot.drivetrain.inputSquared(Robot.oi.driverGamepad.getRightY());
        Robot.drivetrain.tankDrive(-left, -right); // Drive
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
        if (Robot.drivetrain.autoGearShiftingState) {
            Robot.drivetrain.overrideAutoGearShifting = false;
        }
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
