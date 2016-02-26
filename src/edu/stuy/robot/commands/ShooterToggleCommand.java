package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterToggleCommand extends Command {

	public ShooterToggleCommand() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.shooter);
		requires(Robot.redSignalLight);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		boolean shooterOn = Robot.shooter.toggle();
		if (!shooterOn) {
            // If they've finished shooting, make sure
            // the ready-to-shoot light is off
			Robot.redSignalLight.setOff();
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
