package edu.stuy.robot.commands;

import static edu.stuy.robot.RobotMap.CAMERA_FRAME_PX_HEIGHT;
import static edu.stuy.robot.RobotMap.CAMERA_VIEWING_ANGLE_X;
import static edu.stuy.robot.RobotMap.MAX_DEGREES_OFF_AUTO_AIMING;

import java.util.Arrays;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RotateToAimCommand extends Command {

    private boolean goalInFrame;
    private boolean forceStopped = false;

    private double desiredAngle;
    private boolean priorGearShiftState;

    private static double pxOffsetToDegrees(double px) {
        return CAMERA_VIEWING_ANGLE_X * px / CAMERA_FRAME_PX_HEIGHT;
    }

    public RotateToAimCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.resetGyro();
        long start = System.currentTimeMillis();
        double[] reading = Robot.vision.processImage();
        System.out.println("Image processing took " + (System.currentTimeMillis() - start) + "ms");
        if (reading == null) {
            goalInFrame = false;
            System.out.println("Reading was NULL");
        } else {
            desiredAngle = pxOffsetToDegrees(reading[0]);
            System.out.println("Reading was: " + Arrays.toString(reading));
            System.out.println("Desired Angle Delta: " + desiredAngle);;
        }
        priorGearShiftState = Robot.drivetrain.gearUp;
        Robot.drivetrain.manualGearShift(true);
    }

    private double howFarHaveWeCome() {
        if (desiredAngle < 0) {
            return (360 - Robot.drivetrain.getGyroAngle()) / desiredAngle;
        }
        return Robot.drivetrain.getGyroAngle() / desiredAngle;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Robot.oi.driverGamepad.getRightButton().get()) {
            forceStopped = true;
            return;
        }

        // Simplicity: double speed = 0.5;
        double speed = 0.9 - 0.5 * howFarHaveWeCome();
        // right is negative when turning right
        if (desiredAngle < 0) {
            Robot.drivetrain.tankDrive(-speed, speed);
        } else {
            Robot.drivetrain.tankDrive(speed, -speed);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        // When no more can or should be done:
        if (forceStopped || !goalInFrame) {
            Robot.cvSignalLight.setOff();
            return true;
        }

        // Judgement of success:
        double degsOff;
        if (desiredAngle < 0) {
            degsOff = Robot.drivetrain.getGyroAngle() - (360 + desiredAngle);
        } else {
            degsOff = Robot.drivetrain.getGyroAngle() - desiredAngle;
        }
        SmartDashboard.putNumber("CV degrees off", degsOff);

        boolean onTarget = Math.abs(degsOff) < MAX_DEGREES_OFF_AUTO_AIMING;
        Robot.cvSignalLight.set(onTarget);
        return onTarget;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.tankDrive(0.0, 0.0);
        // Set drivetrain gearshift to how it was before aiming
        Robot.drivetrain.manualGearShift(priorGearShiftState);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}