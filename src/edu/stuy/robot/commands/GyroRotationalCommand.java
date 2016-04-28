package edu.stuy.robot.commands;

import static edu.stuy.robot.RobotMap.CAMERA_VIEWING_ANGLE_X;
import static edu.stuy.robot.RobotMap.MAX_DEGREES_OFF_AUTO_AIMING;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Abstract command for rotating a certain number of degrees.
 * The angle to rotate is determined at runtime during initialize,
 * by the abstract method <code>getDesiredAngle</code>
 * @author Berkow
 *
 */
public abstract class GyroRotationalCommand extends Command {

    protected double desiredAngle;
    protected boolean canProceed; // E.g., whether goal is in frame

    private boolean forceStopped; // When operator force stops
    private boolean abort; // When there is an error in a method

    private boolean priorGearShiftState;

    public GyroRotationalCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    protected abstract void setDesiredAngle();

    // Called just before this Command runs the first time
    protected void initialize() {
        try {
            forceStopped = false;
            abort = false;
            priorGearShiftState = Robot.drivetrain.gearUp;
            Robot.drivetrain.manualGearShift(true);
            Robot.drivetrain.resetGyro();

            // Set defaults for values accessible by setDesiredAngle
            desiredAngle = 0.0;
            canProceed = true; // Proceed by default
            setDesiredAngle();
        } catch (Exception e) {
            System.out.println("Error in intialize in RotateToAimCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // INCREASE these if it is OVERshooting
    // DECREASE these if it is UNDERshooting
    private double TUNE_FACTOR = 1;//.1;
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

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        try {
            if (Robot.oi.driverIsOverriding()) {
                forceStopped = true;
                return;
            }
            if (!forceStopped) {
                double speed = 0.65; // + 0.25 * Math.pow(howMuchWeHaveToGo(), 2);
                System.out.println("\n\n\n\n\n\n\nSpeed to use:\t" + speed);
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
            if (forceStopped || abort || !canProceed || Math.abs(desiredAngle) < 0.001) { // last condition for cases when it is zero
                Robot.cvSignalLight.setOff();
                System.out.println("\n\n\n\n\n\n\nforce stopped: " + forceStopped + "\ngoalInFrame: " + canProceed + "\ndesiredAngle: " + desiredAngle);
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