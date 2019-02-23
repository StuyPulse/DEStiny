package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.stuy.util.BoolBox;
import edu.wpi.first.wpilibj.command.Command;

/**
 * All commands causing automatic movement of the drivetrain
 * should have a force stop, and this helps with coordinating
 * shared force-stop across sequenced Commands in a CommandGroup
 * (via forceStoppedController).
 */
abstract public class AutoMovementCommand extends Command {

    private boolean forceStoppedLocal;
    private BoolBox forceStoppedController;
    private boolean usingController;

    public AutoMovementCommand() {
        forceStoppedLocal = false;
        usingController = false;
    }

    public AutoMovementCommand(BoolBox controller) {
        forceStoppedController = controller;
        usingController = true;
    }

    protected boolean getForceStopped() {
        if (forceStoppedController == null) {
            return forceStoppedLocal;
        }
        return forceStoppedController.get();
    }

    private void setForceStopped(boolean newVal) {
        if (forceStoppedController == null) {
            forceStoppedLocal = newVal;
        }
        forceStoppedController.set(newVal);
    }
    

    // Called just before this Command runs the first time
    protected void initialize() {
        if (!usingController) {
            forceStoppedLocal = false;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Robot.oi.driverIsOverriding()) {
            setForceStopped(true);
        }
    }

    abstract protected boolean isFinished();

    protected boolean externallyStopped() {
        return forceStoppedController != null && forceStoppedController.get();
    }

    // Called once after isFinished returns true
    abstract protected void end();

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    abstract protected void interrupted();
}
