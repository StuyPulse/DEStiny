package edu.stuy.robot.commands.auton;

import static edu.stuy.robot.RobotMap.SONAR_DISTANCE_THRESHOLD;
import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightWithSonarCommand extends Command {

    private double inches;
    private double difference;
    private double speedChange = 0.15;
    private double slowSpeed = 0.5;
    private double fastSpeed = slowSpeed + speedChange;

    // Inches represents the number of inches from the tower you want to be from
    // the tower
    public DriveStraightWithSonarCommand(double inches) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        requires(Robot.sonar);
        this.inches = inches;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        double[] data = Robot.sonar.getData();
        double left = data[0];
        double right = data[1];
        difference = left - right;
        double average = (data[0] + data[1]) / 2;
        if (inches > average) {
            slowSpeed *= -1;
            fastSpeed *= -1;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double[] data = Robot.sonar.getData();
        double left = data[0];
        double right = data[1];
        double currentDifference = left - right;
        if (currentDifference > difference + 0.5) {
            Robot.drivetrain.tankDrive(fastSpeed, slowSpeed);
        } else if (currentDifference < difference - 0.5) {
            Robot.drivetrain.tankDrive(slowSpeed, fastSpeed);
        } else {
            Robot.drivetrain.tankDrive(slowSpeed, slowSpeed);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        double[] data = Robot.sonar.getData();
        double average = (data[0] + data[1]) / 2;
        return Math.abs(average - inches) < SONAR_DISTANCE_THRESHOLD;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
