package edu.stuy.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PassChevalCommand extends CommandGroup {
	private static final int MAX_DISTANCE_FORWARD_IN_INCHES = 48;
	private static final int MAX_TIME_FORWARD_IN_SECONDS = 10;
	private static final double SPEED = 1.0; 
	
	public PassChevalCommand() {
		addSequential(new AcquirerMoveToAngleCommand(90));
		addSequential(new DriveForwardCommand(MAX_DISTANCE_FORWARD_IN_INCHES, MAX_TIME_FORWARD_IN_SECONDS, SPEED));
	}
}
