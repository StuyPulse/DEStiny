package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Abstract command for moving forward or back by a displacement
 * determined at runtime by one call to the <code>setInchesToMove</code>
 * method implemented by the subclass.
 * @author Berkow
 */
public abstract class EncoderDrivingCommand extends Command {

    protected double initialInchesToMove; // positive is forward
    protected boolean cancelCommand;

    private boolean abort;

    abstract protected void setInchesToMove();

    // Called just before this Command runs the first time
    protected void initialize() {
        try {
            Robot.drivetrain.resetEncoders();
            initialInchesToMove = 0.0;
            cancelCommand = false;
            abort = false;
            setInchesToMove();
        } catch (Exception e) {
            System.out.println("Error in initialize in EncoderDrivingCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // max speed is 0.8 motor value
    private final static double distForMaxSpeed = 5 * 12.0;

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        try {
            double inchesToGo = inchesToMove();
            double speed = 0.55 + 0.3 * Math.min(1.0, Math.pow(inchesToGo / distForMaxSpeed, 2));
            // The above speed calculation is based on the one that has worked for GyroRotationalCommand
            System.out.println("Inches to go: " + inchesToGo);
            speed *= Math.signum(initialInchesToMove);
            Robot.drivetrain.tankDrive(speed, speed);
        } catch (Exception e) {
            System.out.println("Error in execute in EncoderDrivingCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (abort || cancelCommand || Robot.oi.driverIsOverriding()) {
            return true;
        }
        return Math.abs(inchesToMove()) <= 3.0;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.tankDrive(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.drivetrain.tankDrive(0.0, 0.0);
    }

    private double inchesToMove() {
        // Encoders only return nonnegative values
        return Math.abs(initialInchesToMove) - Robot.drivetrain.getDistance();
    }
}
