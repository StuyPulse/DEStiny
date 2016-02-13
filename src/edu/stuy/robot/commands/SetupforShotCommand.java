package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import static edu.stuy.robot.RobotMap.*;
import edu.stuy.util.TegraDataReader;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetupforShotCommand extends Command {
	private TegraDataReader reader;
	private double[] currentReading;

	private static double pxOffsetToDegrees(double px) {
		return CAMERA_VIEWING_ANGLE_X * px / CAMERA_FRAME_PX_WIDTH;
	}

	public SetupforShotCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.shooter);
		requires(Robot.drivetrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		// Robot.shooter.setSpeedTesting(0.7);
		reader = new TegraDataReader();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		currentReading = reader.readVector();
		double degsOff = pxOffsetToDegrees(currentReading[0]);
		// TODO: Do real math, write non-wack calculation of rightWheelSpeed
		double rightWheelSpeed = -degsOff / (CAMERA_FRAME_PX_WIDTH / 2);
		Robot.drivetrain.tankDrive(-rightWheelSpeed, rightWheelSpeed);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		double degsOff = pxOffsetToDegrees(currentReading[0]);
		return Math.abs(degsOff) < MAX_DEGREES_OFF_AUTO_AIMING;
	}

	// Called once after isFinished returns true
	protected void end() {
		reader.closePort();
		reader = null;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		reader.closePort();
	}
}
