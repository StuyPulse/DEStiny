package edu.stuy.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RotateToAimMultiCommand extends CommandGroup {

    public RotateToAimMultiCommand() {
        // First rotation:
        addSequential(new RotateToAimCommand());
        // Refine:
        addSequential(new RotateToAimCommand(true));
    }

    public RotateToAimMultiCommand(double tolerance) {
        // First rotation:
        addSequential(new RotateToAimCommand(false, tolerance));
        // Refine:
        addSequential(new RotateToAimCommand(true));
    }
}
