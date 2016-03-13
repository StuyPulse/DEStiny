package edu.stuy.robot.commands;

import static edu.stuy.robot.RobotMap.CAMERA_FRAME_PX_WIDTH;
import static edu.stuy.robot.RobotMap.CAMERA_VIEWING_ANGLE_X;
import static edu.stuy.robot.RobotMap.MAX_DEGREES_OFF_AUTO_AIMING;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * DO NOT USE, as this depends on the gyro, which is not functional as of the
 * Thursday before Javitz.
 * @author Wilson
 *
 */
public class RotateToAimWithGyroCommand extends Command {

    private boolean goalInFrame;
    private boolean forceStopped = false;

    private int angle;

    private static int pxOffsetToDegrees(double px) {
        return (int) (CAMERA_VIEWING_ANGLE_X * px / CAMERA_FRAME_PX_WIDTH);
    }

    public RotateToAimWithGyroCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    // TODO: Use commands.auton.RotateDrivetrainCommand, from master. This rips off that.

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.resetGyro();
        double[] reading = Robot.getLatestTegraVector();
        if (reading == null) {
            goalInFrame = false;
        } else {
            angle = pxOffsetToDegrees(reading[0]);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Robot.oi.operatorGamepad.getStartButton().get()) {
            forceStopped = true;
            return;
        }

        // right is negative when turning right
        if (angle < 0) {
            Robot.drivetrain.tankDrive(-0.5, 0.5);
        } else {
            Robot.drivetrain.tankDrive(0.5, -0.5);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
        /* Use when gyro is fixed:
        if (forceStopped || !goalInFrame) {
            return true;
        }
        if (angle < 0) {
            return Math.abs(Robot.drivetrain.getGyroAngle() - (360 + angle)) < 1.0;
        }
        return Math.abs(Robot.drivetrain.getGyroAngle() - angle) < 1.0;
        */
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
