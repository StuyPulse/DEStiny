package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/*
 * Moves forward enough to touch the obstacle
 */
public class ReachObstacleCommand extends DriveForwardCommand {

	private static final int MAX_DISTANCE_IN_INCHES = 2;
	private static final int MAX_TIME_IN_SECONDS = 2;
	
	public ReachObstacleCommand() {
		super(MAX_DISTANCE_IN_INCHES, MAX_TIME_IN_SECONDS);
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
