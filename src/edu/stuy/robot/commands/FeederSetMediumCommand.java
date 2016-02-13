package edu.stuy.robot.commands;

import edu.stuy.robot.*;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class FeederSetMediumCommand extends Command {

    public FeederSetMediumCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.feeder);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.feeder.setMedium();
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
