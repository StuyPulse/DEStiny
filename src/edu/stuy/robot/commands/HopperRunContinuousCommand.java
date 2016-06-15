package edu.stuy.robot.commands;

import edu.stuy.robot.*;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class HopperRunContinuousCommand extends Command {

    private boolean feed;

    public HopperRunContinuousCommand(boolean in) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.hopper);
        feed = in;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("IN INITIALIZE OF HOPPER RUN");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (feed) {
            System.out.println("FEEDING HOPPER");
            Robot.hopper.feed();
        } else {
            System.out.println("SPITTING OUT BAD FOOD");
            Robot.hopper.vomit();
        }
        Robot.hopper.runHopperSensor();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
