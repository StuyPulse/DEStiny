package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/*
 * Moves forward enough to touch the obstacle
 */
public class ReachObstacleCommand extends Command {

	private static final int MAX_DISTANCE = 2;
	private static final int MAX_TIME = 2;
	public double startTime; 
	
	@Override
	protected void initialize() {
		startTime = Timer.getFPGATimestamp();	
	}

	@Override
	protected void execute() {
		//Short distance so don't drive in full speed
		Robot.drivetrain.tankDrive(.5 , .5);
	}

	@Override
	protected boolean isFinished() {
		double distance = Robot.drivetrain.getDistance();
		//Stop the robot if it runs too long
		if (Timer.getFPGATimestamp() - startTime > MAX_TIME) {
			return true;
		}
		//Stop the robot if the distance has been reached
		return distance >= MAX_DISTANCE;
	}

	@Override
	protected void end() {
		Robot.drivetrain.stop();
	}

	@Override
	protected void interrupted() {	
	}

}
