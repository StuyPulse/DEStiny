package edu.stuy.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PassChevalCommand extends CommandGroup {
    private static final int INITIAL_DISTANCE = 90;
    private static final int INITIAL_TIME = 10;
    private static final double INITIAL_SPEED = 0.5;

    private static final int FINAL_DISTANCE = 150;
    private static final int FINAL_TIME = 10;
    private static final double FINAL_SPEED = 1.0;

    public PassChevalCommand() {
        addSequential(new DriveForwardCommand(INITIAL_DISTANCE, INITIAL_TIME, INITIAL_SPEED));
        addSequential(new DropDownMoveToAngleCommand(0), 2.0);
        addSequential(new DriveForwardCommand(FINAL_DISTANCE, FINAL_TIME, FINAL_SPEED));
    }
}
