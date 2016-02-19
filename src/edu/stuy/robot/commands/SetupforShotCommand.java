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
	private boolean goalInFrame;
	private boolean stopAiming = false;

	private static double pxOffsetToDegrees(double px) {
		return CAMERA_VIEWING_ANGLE_X * px / CAMERA_FRAME_PX_WIDTH;
	}

	public SetupforShotCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.shooter);
		requires(Robot.drivetrain);
		requires(Robot.cvSignalLights);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		// Robot.shooter.setSpeedTesting(0.7);
		reader = new TegraDataReader();
		goalInFrame = true; // Assume it is there until we see otherwise
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (!stopAiming) {
			if (currentReading == null) {
			    goalInFrame = false;
			    Robot.cvSignalLights.setNotInFrame();
			    return;
			}
			Robot.cvSignalLights.setInFrame();
			currentReading = reader.readVector();
			double degsOff = pxOffsetToDegrees(currentReading[0]);
			// TODO: Do real math; write non-wack calculation of rightWheelSpeed
			double rightWheelSpeed = -degsOff / (CAMERA_FRAME_PX_WIDTH / 2);
			Robot.drivetrain.tankDrive(-rightWheelSpeed, rightWheelSpeed);
		}
		if (Robot.oi.operatorGamepad.getStartButton().get()) {
			stopAiming = true;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
	    if (!goalInFrame) {
	        return true;
	    }
	    Robot.cvSignalLights.setReadyToShoot();
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
