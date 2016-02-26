package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import static edu.stuy.robot.RobotMap.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SetupforShotCommand extends Command {

    private double[] currentReading;
    private boolean goalInFrame;
    private boolean forceStopped = false;

    private static double pxOffsetToDegrees(double px) {
        return CAMERA_VIEWING_ANGLE_X * px / CAMERA_FRAME_PX_WIDTH;
    }

    public SetupforShotCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        goalInFrame = true; // Assume it is there until we see otherwise
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Robot.oi.operatorGamepad.getStartButton().get()) {
            forceStopped = true;
        }
        if (!forceStopped) {
            currentReading = Robot.readTegraVector();
            SmartDashboard.putBoolean("CV| Goal in frame?", currentReading != null);
            if (currentReading == null) {
                goalInFrame = false;
                return;
            }
            SmartDashboard.putNumber("CV| vector X", currentReading[0]);
            SmartDashboard.putNumber("CV| vector Y", currentReading[1]);
            SmartDashboard.putNumber("CV| bounding rect angle", currentReading[2]);
            double degsOff = pxOffsetToDegrees(currentReading[0]);
            // TODO: Do real math; write non-wack calculation of rightWheelSpeed
            // Set rightWheelSpeed to the ratio of how far off it
            // is to how far it could possibly be off
            double rightWheelSpeed = capWithinOne(-degsOff / (CAMERA_FRAME_PX_WIDTH / 2) * 300);
            // Test with the following modification, or similar ones:
            // rightWheelSpeed = Math.signum(rightWheelSpeed) * Math.pow(rightWheelSpeed, 2);
            SmartDashboard.putNumber("CV| rightWheelSpeed to use", rightWheelSpeed);
            System.out.println("\n\n\n\n\n\nTelling DT to move");

            Robot.drivetrain.tankDrive(-rightWheelSpeed, rightWheelSpeed);
        }
    }

    private double capWithinOne(double x) {
        return Math.signum(x) * Math.min(Math.abs(x), 1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !forceStopped;
        //if (!goalInFrame || forceStopped) {
        //    return true;
        //}
        //double degsOff = pxOffsetToDegrees(currentReading[0]);
        //return Math.abs(degsOff) < MAX_DEGREES_OFF_AUTO_AIMING;
    }

    // Called once after isFinished returns true
    protected void end() {
        // TODO: Handle goalInFrame being false (e.g. with the LEDs)
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
