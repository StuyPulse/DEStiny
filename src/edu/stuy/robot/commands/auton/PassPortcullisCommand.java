/**package edu.stuy.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PassPortcullisCommand extends CommandGroup {

	private static final double MAX_DISTANCE_FORWARD_IN_INCHES = 48;
	private static final double MAX_TIME_FORWARD_IN_SECONDS = 10;
	
	public PassPortcullisCommand() {
		addSequential(new AcquirerMoveToAngleCommand(90));
		addSequential(new DriveForwardCommand(MAX_DISTANCE_FORWARD_IN_INCHES, MAX_TIME_FORWARD_IN_SECONDS));
		addParallel(new AcquirerMoveToAngleCommand(45));
		addSequential(new DriveForwardCommand(MAX_DISTANCE_FORWARD_IN_INCHES, MAX_TIME_FORWARD_IN_SECONDS));
	}
}*/