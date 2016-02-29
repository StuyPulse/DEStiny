package edu.stuy.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PassChevalCommand extends CommandGroup {
    private static final int INITIAL_DISTANCE_FORWARD_IN_INCHES = 90;
    private static final int INITIAL_TIME_FORWARD_IN_SECONDS = 10;
    private static final double INITIAL_SPEED = 0.5;
    private static final int SECOND_DISTANCE_FORWARD_IN_INCHES = 150;
    private static final int SECOND_TIME_FORWARD_IN_SECONDS = 10;
    private static final double SECOND_SPEED = 1.0;

    public PassChevalCommand() {
        addSequential(new DriveForwardCommand(INITIAL_DISTANCE_FORWARD_IN_INCHES, INITIAL_TIME_FORWARD_IN_SECONDS, INITIAL_SPEED));
        addSequential(new DropDownMoveToAngleCommand(0), 2.0);
        addSequential(new DriveForwardCommand(SECOND_DISTANCE_FORWARD_IN_INCHES,
                SECOND_TIME_FORWARD_IN_SECONDS, SECOND_SPEED));
    }
}
