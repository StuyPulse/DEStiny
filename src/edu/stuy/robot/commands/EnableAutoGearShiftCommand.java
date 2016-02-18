package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class EnableAutoGearShiftCommand extends Command {

    public EnableAutoGearShiftCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.overrideAutoGearShifting = false;
        Robot.drivetrain.autoGearShiftingState = true;
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
