package edu.stuy.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PassChevalCommand extends CommandGroup {
	// private static final int MAX_DISTANCE_FORWARD_IN_INCHES = 48;
	private static final int MAX_TIME_FORWARD_IN_SECONDS = 10;
	private static final double SPEED = 1.0;

	public PassChevalCommand() {
		// Tune later
		addSequential(new DriveForwardCommand(90.0, 10.0, 0.5));
		addSequential(new DropDownMoveToAngleCommand(0), 2.0);
		addSequential(new DriveForwardCommand(150.0, MAX_TIME_FORWARD_IN_SECONDS, SPEED));
	}
}
