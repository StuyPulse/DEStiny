package edu.stuy.robot.commands.auton;

import static edu.stuy.robot.RobotMap.ROCK_WALL_CURRENT_THRESHOLD;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GoOverRockWallCommand extends CommandGroup {

	// Tune after testing
	private static final int INITIAL_DISTANCE_IN_INCHES = 48;
	private static final int INITIAL_TIME_IN_SECONDS = 10;
	private static final double INITIAL_SPEED = 0.5;
	private static final int FINAL_DISTANCE_IN_INCHES = 48;
	private static final int FINAL_TIME_IN_SECONDS = 10;
	private static final double FINAL_SPEED = 1.0;

	public GoOverRockWallCommand() {
		while (Robot.drivetrain.getAverageCurrent() < ROCK_WALL_CURRENT_THRESHOLD) {
			Robot.drivetrain.tankDrive(0.5, 0.5);
		}
		addSequential(new DriveBackwardCommand(INITIAL_DISTANCE_IN_INCHES, INITIAL_TIME_IN_SECONDS, INITIAL_SPEED));
		addSequential(new DriveForwardCommand(FINAL_DISTANCE_IN_INCHES, FINAL_TIME_IN_SECONDS, FINAL_SPEED));
	}
}
