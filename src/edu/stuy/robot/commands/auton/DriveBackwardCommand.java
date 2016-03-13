package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveBackwardCommand extends Command {
	
	public double maxDistanceInInches;
    public double maxTimeInSeconds;
	public double startTime;
	public double motorSpeed;
	
	public DriveBackwardCommand(double distance, double time, double speed) {
		maxDistanceInInches = distance;
		maxTimeInSeconds = time;
		motorSpeed = speed;
	}
	
	@Override
	protected void initialize() {
		startTime = Timer.getFPGATimestamp();
		
	}
	
	@Override
	protected boolean isFinished() {
		double distance = Robot.drivetrain.getDistance();
		if (distance < 0) {
		    // Both encoders are broken
		    return true;
		}
		//Stop the robot if it runs too long
		if (Timer.getFPGATimestamp() - startTime > maxTimeInSeconds) {
			return true;
		}
		//Stop the robot if the distance has been reached
		return -distance >= maxDistanceInInches;
	}
	
	@Override
	protected void end() {
		Robot.drivetrain.stop();
	}

	@Override
	protected void execute() {
		Robot.drivetrain.tankDrive(motorSpeed, motorSpeed);
	}

	@Override
	protected void interrupted() {
	}
}