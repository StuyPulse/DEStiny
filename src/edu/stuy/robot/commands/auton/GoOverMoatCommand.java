package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GoOverMoatCommand extends DriveForwardCommand {

	//private static final int MAX_DISTANCE_IN_INCHES = 48;
	private static final int MAX_TIME_IN_SECONDS = 12;
	private static final double SPEED = 1.0;
	
	public GoOverMoatCommand() {
		super(SmartDashboard.getNumber("Moat"), MAX_TIME_IN_SECONDS, SPEED);
	}

	@Override
	protected void initialize() {
		super.initialize();
		Robot.acquirer.lowerAcquirerToDrivingPosition();
	}

	@Override
	protected void execute() {
		Robot.drivetrain.tankDrive(1.0, 1.0);
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
