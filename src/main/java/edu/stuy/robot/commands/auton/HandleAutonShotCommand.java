package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Checks <code>Robot.cvFoundGoal</code> to determine whether
 * to run the hopper to shoot or to turn off the shooter.
 */
public class HandleAutonShotCommand extends Command {

    public HandleAutonShotCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.hopper);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (Robot.cvFoundGoal) {
            System.out.println("Robot.cvFoundGoal is true. Feeding hopper");
        } else {
            System.out.println("Robot.cvFoundGoal is false. Cutting power to shooter");
            Robot.shooter.stop();
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Robot.cvFoundGoal) {
            Robot.hopper.feed();
            Robot.hopper.runHopperSensor();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        // Continue running until externally timed out (e.g. in a CommandGroup)
        // if the hopper is to run. Otherwise exit immediately.
        return !Robot.cvFoundGoal;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
