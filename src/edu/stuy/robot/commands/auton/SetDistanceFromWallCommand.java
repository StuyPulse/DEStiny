package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Gets to a certain distance to wall
 */
public class SetDistanceFromWallCommand extends Command {

    double distance; // in Inches
    double speed;
    double difference;
    // double originalDistance;// For determining when to stop
    final double CLOSE_ENOUGH = 5;// in Inches

    public SetDistanceFromWallCommand(double distance, double speed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        requires(Robot.sonar);
        this.distance = distance;
        this.speed = speed;
        difference = distance;// Arbitrary initialization
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        // originalDistance = Robot.sonar.getAverageDistance() - distance;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        difference = Robot.sonar.getAverageDistance() - distance;
        Robot.drivetrain.tankDrive(Math.signum(difference) * speed, Math.signum(difference) * speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (Math.abs(difference - distance) < CLOSE_ENOUGH);
    }

    // Called once after isFinished returns true
    protected void end() {
        // Just making sure it stops when it's done.
        Robot.drivetrain.tankDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
