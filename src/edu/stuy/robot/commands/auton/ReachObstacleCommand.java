package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;

/*
 * Moves forward enough to touch the obstacle
 */
public class ReachObstacleCommand extends DriveForwardCommand {

	private static final int MAX_DISTANCE_IN_INCHES = 2;
	private static final int MAX_TIME_IN_SECONDS = 2;
	private static final double SPEED = 0.5;
	
	public ReachObstacleCommand() {
		super(MAX_DISTANCE_IN_INCHES, MAX_TIME_IN_SECONDS, SPEED);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
	}

	@Override
	protected void execute() {
		//Short distance so don't drive in full speed
		Robot.drivetrain.tankDrive(.5 , .5);
	}

	@Override
	protected boolean isFinished() {
		return super.isFinished();
	}

	@Override
	protected void end() {
		super.end();
	}

	@Override
	protected void interrupted() {	
	}

}
