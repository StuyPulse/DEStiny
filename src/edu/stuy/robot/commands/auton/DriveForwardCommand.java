package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveForwardCommand extends Command {

	public double maxDistanceInInches;
	public double maxTimeInSeconds;
	public double startTime;

	public DriveForwardCommand(double distance, double time) {
		maxDistanceInInches = distance;
		maxTimeInSeconds = time;
	}

	@Override
	protected void initialize() {
		startTime = Timer.getFPGATimestamp();

	}

	@Override
	protected boolean isFinished() {
		double distance = Robot.drivetrain.getDistance();
		// Stop the robot if it runs too long
		if (Timer.getFPGATimestamp() - startTime > maxTimeInSeconds) {
			return true;
		}
		// Stop the robot if the distance has been reached
		return distance >= maxDistanceInInches;
	}

	@Override
	protected void end() {
		Robot.drivetrain.stop();
		Robot.drivetrain.resetEncoders();
	}

	@Override
	protected void execute() {
		Robot.drivetrain.tankDrive(1, 1);
	}

	@Override
	protected void interrupted() {
	}
}