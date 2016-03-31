package edu.stuy.robot.commands;

import static edu.stuy.robot.RobotMap.CAMERA_FRAME_PX_WIDTH;
import static edu.stuy.robot.RobotMap.CAMERA_VIEWING_ANGLE_X;
import static edu.stuy.robot.RobotMap.MAX_DEGREES_OFF_AUTO_AIMING;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Incrementally rotate the drivetrain until a high goal
 * is centered in thecamera's frame
 */
public class RotateToGoalCommand extends Command {

    private double[] currentReading;
    private boolean goalInFrame;
    private boolean forceStopped = false;
    private boolean priorGearShiftState;

    private static double pxOffsetToDegrees(double px) {
        return CAMERA_VIEWING_ANGLE_X * px / CAMERA_FRAME_PX_WIDTH;
    }

    public RotateToGoalCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        goalInFrame = true; // Assume it is there until we see otherwise
        forceStopped = false;
        priorGearShiftState = Robot.drivetrain.gearUp;
        Robot.drivetrain.manualGearShift(true);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (Robot.oi.driverGamepad.getRightButton().get()) {
            forceStopped = true;
            System.out.println("Force stopped by driver");
        }
        if (!forceStopped) {
            currentReading = Robot.getLatestTegraVector();
            SmartDashboard.putBoolean("CV| Goal in frame?", currentReading != null);
            if (currentReading == null) {
                goalInFrame = false;
                return;
            }
            SmartDashboard.putNumber("CV| vector X", currentReading[0]);
            SmartDashboard.putNumber("CV| vector Y", currentReading[1]);
            SmartDashboard.putNumber("CV| bounding rect angle", currentReading[2]);
            double degsOff = pxOffsetToDegrees(currentReading[0]);
            double rightWheelSpeed = clampMagnitude(degsOff / (CAMERA_FRAME_PX_WIDTH / 2) * 50, SmartDashboard.getNumber("minCV"), SmartDashboard.getNumber("maxCV"));
            //rightWheelSpeed = Math.signum(rightWheelSpeed) * 0.7;
            // Try with the following modification, or similar ones:
            // rightWheelSpeed = Math.signum(rightWheelSpeed) * Math.pow(rightWheelSpeed, 2);
            SmartDashboard.putNumber("CV| rightWheelSpeed to use", rightWheelSpeed);

            System.out.println("Moving with rwspeed: " + rightWheelSpeed);
            Robot.drivetrain.tankDrive(-rightWheelSpeed, rightWheelSpeed);
        }
    }

    private double clampMagnitude(double x, double minMag, double maxMag) {
        // minMag and maxMag better be nonnegative
        return Math.signum(x) * Math.min(maxMag, Math.max(minMag, Math.abs(x)));
    }

    private double clampWithinOne(double x) {
        return Math.signum(x) * Math.min(Math.abs(x), 1);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (!goalInFrame || forceStopped) {
            return true;
        }
        double degsOff = pxOffsetToDegrees(currentReading[0]);
        boolean onTarget = Math.abs(degsOff) < MAX_DEGREES_OFF_AUTO_AIMING;
        Robot.cvSignalLight.set(onTarget);
        return onTarget;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.drivetrain.tankDrive(0.0, 0.0);
        // Set drivetrain gearshift to how it was before aiming
        Robot.drivetrain.manualGearShift(priorGearShiftState);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
