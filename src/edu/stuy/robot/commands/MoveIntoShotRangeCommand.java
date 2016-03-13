package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static edu.stuy.robot.RobotMap.IDEAL_VERTICAL_OFFSET_AUTO_AIMING;
import static edu.stuy.robot.RobotMap.MAX_VERTICAL_PX_OFF_AUTO_AIMING;
import static edu.stuy.robot.RobotMap.CAMERA_FRAME_PX_HEIGHT;

/**
 *
 */
public class MoveIntoShotRangeCommand extends Command {

    private double[] currentReading;
    private boolean forceStopped;
    private boolean goalLeftFrame;
    private boolean readyForShot;

    public MoveIntoShotRangeCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        forceStopped = false;
        // Goal should be in frame at start
        goalLeftFrame = false;
        readyForShot = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Robot.oi.operatorGamepad.getStartButton().get()) {
            forceStopped = true;
        }
        if (!forceStopped) {
            currentReading = Robot.getLatestTegraVector();
            SmartDashboard.putBoolean("CV| Goal in frame?", currentReading != null);
            if (currentReading == null) {
                goalLeftFrame = true;
                return;
            }
            SmartDashboard.putNumber("CV| vector X", currentReading[0]);
            SmartDashboard.putNumber("CV| vector Y", currentReading[1]);
            SmartDashboard.putNumber("CV| bounding rect angle", currentReading[2]);
            double verticalOffset = currentReading[1];
            double offsetFromIdeal = IDEAL_VERTICAL_OFFSET_AUTO_AIMING - verticalOffset;
            if (Math.abs(offsetFromIdeal) < MAX_VERTICAL_PX_OFF_AUTO_AIMING) {
                readyForShot = true;
                return;
            }
            // Set wheelSpeed to ratio of how much off to how much could possibly be off
            double wheelSpeed = offsetFromIdeal / (CAMERA_FRAME_PX_HEIGHT / 2 + IDEAL_VERTICAL_OFFSET_AUTO_AIMING);
            SmartDashboard.putNumber("CV| wheelSpeed to use", wheelSpeed);
            Robot.drivetrain.tankDrive(wheelSpeed, wheelSpeed);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return readyForShot || goalLeftFrame || forceStopped;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
