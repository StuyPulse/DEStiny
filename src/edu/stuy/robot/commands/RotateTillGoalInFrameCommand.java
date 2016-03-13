package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Incrementally rotate the drivetrain in a given direction
 * until a goal is in the frame.
 */
public class RotateTillGoalInFrameCommand extends Command {

    private boolean goalInFrame;
    private boolean forceStopped = false;
    private boolean turnRight;
    private static final double searchingSpeed = 0.3;

    public RotateTillGoalInFrameCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        turnRight = true;
    }
    
    public RotateTillGoalInFrameCommand(boolean turnRight) {
        this.turnRight = turnRight;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        goalInFrame = false; // Assume it is not there until we see otherwise
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (Robot.oi.driverGamepad.getStartButton().get()) {
            forceStopped = true;
        }
        if (!forceStopped) {
            goalInFrame = Robot.getLatestTegraVector() != null;
            SmartDashboard.putBoolean("CV| Goal in frame?", goalInFrame);
            if (!goalInFrame) {
                if (turnRight) {
                    Robot.drivetrain.tankDrive(searchingSpeed, -searchingSpeed);
                } else {
                    Robot.drivetrain.tankDrive(-searchingSpeed, searchingSpeed);
                }
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return goalInFrame || forceStopped;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
