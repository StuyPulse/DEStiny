package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveForwardCommand extends Command {

    public double maxDistanceInInches;
    public double maxTimeInSeconds;
    public double startTime;
    public double motorSpeed;

    public DriveForwardCommand(double distance, double time, double speed) {
        requires(Robot.drivetrain);
        maxDistanceInInches = distance;
        maxTimeInSeconds = time;
        motorSpeed = speed;
    }

    @Override
    protected void initialize() {
        startTime = Timer.getFPGATimestamp();
        Robot.drivetrain.resetEncoders();
    }

    @Override
    protected boolean isFinished() {
        double distance = Robot.drivetrain.getDistance();
        if (distance < 0) {
            return true;
        }
        // Stop the robot if it runs too long
        if (Timer.getFPGATimestamp() - startTime > maxTimeInSeconds) {
            return true;
        }
        // Stop the robot if the distance has been reached
        return distance >= maxDistanceInInches;
    }

    @Override
    protected void end() {
        Robot.drivetrain.stop();
        Robot.drivetrain.resetEncoders();
    }

    @Override
    protected void execute() {
        Robot.drivetrain.tankDrive(motorSpeed, motorSpeed);
    }

    protected void setMaxDistanceInInches(double distance) {
        maxDistanceInInches = distance;
    }

    @Override
    protected void interrupted() {
    }
}