package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class GoOverRockWallCommand extends DriveForwardCommand {

	private static final int MAX_DISTANCE_IN_INCHES = 48;
	private static final int MAX_TIME_IN_SECONDS = 10;
	
	public GoOverRockWallCommand() {
		super(MAX_DISTANCE_IN_INCHES, MAX_TIME_IN_SECONDS);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		Robot.acquirer.lowerAcquirerToDrivingPosition();
	}

	@Override
	protected void execute() {
		Robot.drivetrain.tankDrive(1 , 1);
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
