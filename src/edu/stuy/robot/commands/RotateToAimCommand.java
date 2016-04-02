package edu.stuy.robot.commands;

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

    private boolean priorGearShiftState;

    private static final long timeout = 5000; // currently not in use
    private double[] cvReading;
    private double desiredAngle;
    private long timeStart;

    private static final int maxSprints = 2;
    private int sprintsDone;
    // A sprint is reading CV and then rotating until "on target"

    public RotateToAimCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    private void initialSetup() {
        forceStopped = false;
        abort = false;
        sprintsDone = 0;
        timeStart = System.currentTimeMillis();
        priorGearShiftState = Robot.drivetrain.gearUp;
        Robot.drivetrain.manualGearShift(true);
    }

    private void sprintSetup() {
        desiredAngle=0.0;
        Robot.drivetrain.resetGyro();

        long start = System.currentTimeMillis();
        cvReading = Robot.vision.processImage();
        System.out.println("Image processing took " + (System.currentTimeMillis() - start) + "ms");

        goalInFrame = true;
        desiredAngle = SmartDashboard.getNumber("cv-angle");
        /*goalInFrame = cvReading != null;
        SmartDashboard.putString("cv-reading", Arrays.toString(cvReading));
        if (goalInFrame) {
            desiredAngle = StuyVisionModule.frameXPxToDegrees(cvReading[0]);
            SmartDashboard.putNumber("cv-angle", desiredAngle);
            System.out.println("Reading was: " + Arrays.toString(cvReading) + "-----------------------------");
            System.out.println("Desired Angle Delta: " + desiredAngle);
        } else {
            System.out.println("Reading was NULL------------------------------------------------------------");
        }
        SmartDashboard.putBoolean("cv-visible", goalInFrame);*/
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        try {
            initialSetup();
            sprintSetup();
        } catch (Exception e) {
            System.out.println("Error in intialize in RotateToAimCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // INCREASE these if it is OVERshooting
    // DECREASE these if it is UNDERshooting
    private double TUNE_FACTOR = 1.1;
    private double TUNE_OFFSET = 0.0;
    private double angleMoved() {
        double gyro = Robot.drivetrain.getGyroAngle();
        if (gyro > 180) {
            return gyro - 360;
        }
        return gyro * TUNE_FACTOR + TUNE_OFFSET;
    }

    private double degreesToMove() {
        return desiredAngle - angleMoved();
    }

    // In some sense, what percent of the way off are we? 100% = 1.0 would be
    // the goal is at left/right edge of the frame; 0% = 0.0 would be on target.
    private double howMuchWeHaveToGo() {
        return Math.abs(degreesToMove() / (CAMERA_VIEWING_ANGLE_X / 2));
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        try {
            if (Robot.oi.driverIsOverriding()) {
                forceStopped = true;
                return;
            }
            if (!forceStopped) {
                double speed = 0.45 + 0.35 * Math.pow(howMuchWeHaveToGo(), 2);
                //System.out.println("\n\n\n\nSpeed to use:\t" + speed);
                System.out.println("getGyroAngle():\t" + Robot.drivetrain.getGyroAngle());
                System.out.println("angleMoved():\t" + angleMoved());
                System.out.println("desiredAngle:\t" + desiredAngle);
                System.out.println("degreesToMove():\t" + degreesToMove());
                //System.out.println("original distance:\t" + StuyVisionModule.findDistanceToGoal(cvReading));
                // right is negative when turning right
                if (degreesToMove() < 0) {
                    System.out.println("\nMoving left, as degreesToMove()=" + desiredAngle + " < 0");
                    System.out.println("So: tankDrive(" + -speed + ", " + speed + ")\n");
                    Robot.drivetrain.tankDrive(-speed, speed);
                } else {
                    System.out.println("\nMoving RIGHT, as degreesToMove()=" + desiredAngle + " > 0");
                    System.out.println("So: tankDrive(" + speed + ", " + -speed + ")\n");
                    Robot.drivetrain.tankDrive(speed, -speed);
                }
            }
        } catch (Exception e) {
            System.out.println("\n\n\n\n\nError in execute in RotateToAimCommand:");
            e.printStackTrace();
            abort = true; // abort command
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
            double degsOff = degreesToMove();
            SmartDashboard.putNumber("CV degrees off", degsOff);

            boolean onTarget = Math.abs(degsOff) < MAX_DEGREES_OFF_AUTO_AIMING;
            System.out.println("degsOff: " + degsOff + "\nonTarget: " + onTarget);
            Robot.cvSignalLight.set(onTarget);

            //if (System.currentTimeMillis() - timeStart > timeout) {
            //    System.out.println("RotateToAimCommand timed out after " + timeout + "ms");
            //    return true;
            //}

            /*if (sprintsDone < maxSprints - 1) {
                sprintSetup();
                sprintsDone += 1;
                System.out.println("\n\n\n\n\nENTERING SPRINT index " + sprintsDone + "!\n\n");
                return false;
            }*/
            return onTarget;
        } catch (Exception e) {
            System.out.println("Error in isFinished in RotateToAimCommand:");
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