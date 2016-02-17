package edu.stuy.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PassPortcullisCommand extends CommandGroup {

	private static final int MAX_DISTANCE_FORWARD_IN_INCHES = 48;
	private static final int MAX_TIME_FORWARD_IN_SECONDS = 10;
	private static final double SPEED = 1.0;
	
	
	public PassPortcullisCommand() {
		addSequential(new DropDownMoveToAngleCommand(90));
		addSequential(new DriveForwardCommand(MAX_DISTANCE_FORWARD_IN_INCHES, MAX_TIME_FORWARD_IN_SECONDS, SPEED));
		addParallel(new DropDownMoveToAngleCommand(45));
		addSequential(new DriveForwardCommand(MAX_DISTANCE_FORWARD_IN_INCHES, MAX_TIME_FORWARD_IN_SECONDS, SPEED));
	}
}