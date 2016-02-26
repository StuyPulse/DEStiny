package edu.stuy.util;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public abstract class OverridableCommand extends Command {

    @Override
    public void start() {
        if (!Robot.dontStartCommands) {
            super.start();
        }
    }

    abstract protected void initialize();

    abstract protected void end();

    abstract protected void execute();

    abstract protected void interrupted();

    abstract protected boolean isFinished();
}
