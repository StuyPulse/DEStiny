package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.stuy.robot.cv.StuyVision;
import edu.wpi.first.wpilibj.command.Command;
import static edu.stuy.robot.RobotMap.CAMERA_VIEWING_ANGLE_X;

/**
 *
 */
public class FollowObjectCommand extends AutoMovementCommand {

    private int count;
    private double angle;
    private double distance;

    private boolean onTarget;
    private boolean done;
    private double[] cvReading;
    private Thread cvThread;

    // max speed is 0.8 motor value
    private final static double distForMaxSpeed = (5 * 12.0) * 5;

    public FollowObjectCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super();
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        super.initialize();
        Robot.drivetrain.resetEncoders();
        Robot.drivetrain.resetGyro();
        count = 0;
        done = false;
        cvThread = new CVThread();
        cvThread.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        super.execute();
        if (!getForceStopped()) {
            onTarget = false;

            if (cvReading == null) {
                // Couldn't find the object
                done = true;
                return;
            }

            // Adjust the angle first, then the distance
            // angle = StuyVision.frameXPxToDegrees(cvReading[0]);
            if (Math.abs(angle) > 8.0) {
                rotate();

            } else {
                // distance = StuyVision.findBotDistanceToGoal(cvReading[1]);
                if (Math.abs(distance) <= 120) {
                    // We're in range
                    onTarget = true;
                    return;
                }

                // We need to adjust our distance
                Robot.drivetrain.resetEncoders();
                adjustDistance();
            }

            if (onTarget) {
                Robot.cvSignalLight.stayOn();
            } else {
                Robot.cvSignalLight.stayOff();
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return getForceStopped();
    }

    // Called once after isFinished returns true
    protected void end() {
        cvThread.interrupt();
        Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        cvThread.interrupt();
        Robot.drivetrain.tankDrive(0.0, 0.0);
    }

    protected void rotate() {
        double speed = 0.53 + 0.08 * Math.pow(howMuchWeHaveToGo(), 2); // test
        System.out.println("Rotation speed: " + speed + "\nhowMuchWeHaveToGo(): " + howMuchWeHaveToGo());
        if (angle > 0) {
            System.out.println("Turning right. Angle:\t" + angle);
            Robot.drivetrain.tankDrive(speed, -speed);
        } else {
            System.out.println("Turning left. Angle:\t" + angle);
            Robot.drivetrain.tankDrive(-speed, speed);
        }
    }

    protected void adjustDistance() {
        double speed = 0.7 + 0.3 * Math.min(1.0, Math.pow(inchesToMove() / distForMaxSpeed, 2)); // test
        System.out.println("Distance: " + distance);
        speed *= Math.signum(distance);
        System.out.println("Speed: " + speed);
        Robot.drivetrain.tankDrive(speed, speed);
    }

    private double howMuchWeHaveToGo() {
        // Used for ramping
        return Math.abs(degreesToMove() / (CAMERA_VIEWING_ANGLE_X / 2));
    }

    private double inchesToMove() {
        return Math.abs(distance) - Robot.drivetrain.getDistance();
    }

    private double degreesToMove() {
        return angle - angleMoved();
    }

    private double angleMoved() {
        double gyro = Robot.drivetrain.getGyroAngle();
        if (gyro > 180) {
            return gyro - 360;
        }
        return gyro;
    }

    private class CVThread extends Thread {

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                cvReading = Robot.vision.processImage();
                if (cvReading != null) {
                    angle = StuyVision.frameXPxToDegrees(cvReading[0]);
                    distance = StuyVision.findBotDistanceToGoal(cvReading[1]);
                    Robot.drivetrain.resetGyro();
                }
            }
        }
    }
}
