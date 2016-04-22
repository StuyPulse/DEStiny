package edu.stuy.robot.commands;

import edu.stuy.robot.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class HopperRunCommand extends Command {

    private boolean feed;
    private boolean useTimeout;
    private double timeout;
    private double startTime;

    public HopperRunCommand(boolean in) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.hopper);
        feed = in;
    }

    public HopperRunCommand(boolean in, double timeout) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.hopper);
        feed = in;
        useTimeout = true;
        this.timeout = timeout;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        startTime = Timer.getFPGATimestamp();
        if (feed) {
            Robot.hopper.feed();
        } else {
            Robot.hopper.vomit();
        }
        Robot.hopper.runHopperSensor();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (useTimeout) {
            double currTime = Timer.getFPGATimestamp();
            if (Timer.getFPGATimestamp() - startTime > timeout) {
                Robot.hopper.stop();
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
