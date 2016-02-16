package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DrivetrainTankDriveCommand extends Command {

	private boolean override;

	public DrivetrainTankDriveCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.drivetrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		override = false;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (Robot.oi.driverGamepad.getRightTrigger().get() || Robot.oi.driverGamepad.getLeftTrigger().get()) {
			override = true;
		} else if (Robot.oi.driverGamepad.getStartButton().get()) {
			override = false;
		}
		System.out.println(override);
		double left = Robot.oi.driverGamepad.getLeftY();
		double right = Robot.oi.driverGamepad.getRightY();
		Robot.drivetrain.autoGearShift(override);
		Robot.drivetrain.tankDrive(-left, -right);
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
