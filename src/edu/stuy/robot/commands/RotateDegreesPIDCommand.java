package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RotateDegreesPIDCommand extends Command {

    private static final PIDController pid = Robot.drivetrain.pid;

    protected double targetAngle;
    private boolean forceStopped;
    private boolean abort;

    public RotateDegreesPIDCommand() {
        requires(Robot.drivetrain);
    }

    public RotateDegreesPIDCommand(double ang) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        targetAngle = ang;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        try {
            forceStopped = false;
            abort = false;
            Robot.drivetrain.resetGyro();
            pid.setSetpoint(targetAngle);
            pid.enable();
        } catch (Exception e) {
            System.out.println("Error in initialize in RotateToAimCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        try {
            if (Robot.oi.driverIsOverriding()) {
                forceStopped = true;
            }
        } catch (Exception e) {
            System.out.println("Error in execute in RotateToAimCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        try {
            if (forceStopped || abort) {
                System.out.println("Exiting RotateToAimPIDCommand. forceStopped=" + forceStopped + ", abort=" + abort);
                return true;
            }
            return pid.onTarget();
        } catch (Exception e) {
            System.out.println("Error in isFinished in RotateToAimCommand:");
            e.printStackTrace();
            return true; // abort
        }
    }

    // Called once after isFinished returns true
    protected void end() {
        try {
            pid.disable();
            Robot.cvSignalLight.set(pid.onTarget());
        } catch (Exception e) {
            System.out.println("Error in end in RotateToAimCommand:");
            e.printStackTrace();
        }
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        try {
            pid.disable();
            Robot.cvSignalLight.set(pid.onTarget());
        } catch (Exception e) {
            System.out.println("Error in interrupted in RotateToAimCommand:");
            e.printStackTrace();
        }
    }
}
