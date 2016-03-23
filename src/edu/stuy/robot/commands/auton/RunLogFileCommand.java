package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.stuy.robot.RobotMap;
import edu.stuy.util.LogData;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunLogFileCommand extends Command {

    private int increment;
    public RunLogFileCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.recorder.readFromFile(RobotMap.LOG_FILE_NAME);
        increment = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        LogData currentData = Robot.recorder.getData(increment);
        Robot.drivetrain.tankDrive(currentData.driveLeft, currentData.driveRight);
        // TODO: Add Other Values Here.
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return increment >= Robot.recorder.logLength();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
