package edu.stuy.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PassDrawbridgeCommand extends CommandGroup {
	private static final int MAX_DISTANCE_BACKWARD_IN_INCHES = 6;
	private static final int MAX_DISTANCE_FORWARD_IN_INCHES = MAX_DISTANCE_BACKWARD_IN_INCHES + 48;
	private static final int MAX_TIME_BACKWARD_IN_SECONDS = 5;
	private static final int MAX_TIME_FORWARD_IN_SECONDS = 10;
	private static final double SPEED = 1.0;
	
	public PassDrawbridgeCommand() {
		
		addSequential(new AcquirerMoveToAngleCommand(90));
		addSequential(new DriveBackwardCommand(MAX_DISTANCE_BACKWARD_IN_INCHES, MAX_TIME_BACKWARD_IN_SECONDS, SPEED));
		addSequential(new AcquirerMoveToAngleCommand(90));
		addSequential(new DriveForwardCommand(MAX_DISTANCE_FORWARD_IN_INCHES, MAX_TIME_FORWARD_IN_SECONDS, SPEED));
	}
}
