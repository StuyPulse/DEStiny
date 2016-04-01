package edu.stuy.robot.commands;

import static edu.stuy.robot.RobotMap.CAMERA_FRAME_PX_HEIGHT;
import static edu.stuy.robot.RobotMap.CAMERA_VIEWING_ANGLE_X;
import static edu.stuy.robot.RobotMap.MAX_DEGREES_OFF_AUTO_AIMING;

import java.util.Arrays;

import edu.stuy.robot.Robot;
import edu.stuy.robot.cv.StuyVisionModule;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RotateToAimCommand extends Command {

    private boolean goalInFrame;
    private boolean forceStopped;
    private boolean abort;

    private double desiredAngle;
    private boolean priorGearShiftState;

    private static final long timeout = 5000;
    private double[] initialReading;
    private long timeStart;

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
        try {
            Robot.drivetrain.resetGyro();
            forceStopped = false;
            abort = false;

            timeStart = System.currentTimeMillis();
            initialReading = Robot.vision.processImage();
            System.out.println("Image processing took " + (System.currentTimeMillis() - timeStart) + "ms");

            goalInFrame = initialReading != null;
            if (goalInFrame) {
                desiredAngle = pxOffsetToDegrees(initialReading[0]);
                System.out.println("Reading was: " + Arrays.toString(initialReading));
                System.out.println("Desired Angle Delta: " + desiredAngle);
            } else {
                System.out.println("Reading was NULL");
            }
            priorGearShiftState = Robot.drivetrain.gearUp;
            Robot.drivetrain.manualGearShift(true);
        } catch (Exception e) {
            System.out.println("Error in intialize in rotatetoaim:");
            e.printStackTrace();
            abort = true;
        }
    }

    // INCREASE these if it is OVERshooting
    // DECREASE these if it is UNDERshooting
    private double TUNE_FACTOR = 0.9;
    private double TUNE_OFFSET = 0.0;
    private double angleMoved() {
        double gyro = Robot.drivetrain.getGyroAngle();
        if (gyro > 180) {
            return gyro - 360;
        }
        return gyro * TUNE_FACTOR + TUNE_OFFSET;
    }

    private double howFarHaveWeCome() {
        return Math.abs(angleMoved() / (CAMERA_VIEWING_ANGLE_X / 2));
    }
    private double howMuchWeHaveToGo() {
        return Math.abs((desiredAngle - angleMoved()) / (CAMERA_VIEWING_ANGLE_X / 2));
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        try {
            if (Robot.oi.driverIsOverriding()) {
                forceStopped = true;
                return;
            }
            if (!forceStopped) {
                //double speed = 0.9 - 0.5 * howFarHaveWeCome();
                double speed = 0.4 + howMuchWeHaveToGo();
                System.out.println("\n\n\n\nSpeed to use: " + speed);
                System.out.println("direct gyro angle: " + Robot.drivetrain.getGyroAngle());
                System.out.println("measured gyro ang(): " + angleMoved());
                System.out.println("desired angle: " + desiredAngle);
                System.out.println("original distance: " + StuyVisionModule.findDistanceToGoal(initialReading));
                // right is negative when turning right
                if (desiredAngle < 0) {
                    System.out.println("Moving left, as desiredAngle=" + desiredAngle + " < 0");
                    Robot.drivetrain.tankDrive(-speed, speed);
                } else {
                    System.out.println("Moving RIGHT, as desiredAngle=" + desiredAngle + " > 0");
                    Robot.drivetrain.tankDrive(speed, -speed);
                }
            }
        } catch (Exception e) {
            System.out.println("\n\n\n\n\nError in execute in rotatetoaim:");
            e.printStackTrace();
            forceStopped = true; // abort command
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        try {
            // When no more can or should be done:
            if (forceStopped || abort || !goalInFrame || Math.abs(desiredAngle) < 0.001) { // last condition for cases when it is zero
                Robot.cvSignalLight.setOff();
                System.out.println("\n\n\n\n\n\n\nforce stopped: " + forceStopped + "\ngoalInFrame: " + goalInFrame + "\ndesiredAngle: " + desiredAngle);
                return true;
            }

            // Judgement of success:
            double degsOff = angleMoved() - desiredAngle;
            SmartDashboard.putNumber("CV degrees off", degsOff);

            boolean onTarget = Math.abs(degsOff) < MAX_DEGREES_OFF_AUTO_AIMING;
            System.out.println("degsOff: " + degsOff + "\nonTarget: " + onTarget);
            Robot.cvSignalLight.set(onTarget);

            //if (System.currentTimeMillis() - timeStart > timeout) {
            //    System.out.println("RotateToAimCommand timed out after " + timeout + "ms");
            //    return true;
            //}

            return onTarget;
        } catch (Exception e) {
            System.out.println("Error in isFinished in rotatetoaim:");
            e.printStackTrace();
            return true; // abort
        }
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.tankDrive(0.0, 0.0);
        System.out.println("ENDED");
        // Set drivetrain gearshift to how it was before aiming
        Robot.drivetrain.manualGearShift(priorGearShiftState);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}