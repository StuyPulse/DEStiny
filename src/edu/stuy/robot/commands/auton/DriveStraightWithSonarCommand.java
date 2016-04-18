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
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double[] data = Robot.sonar.getData();
        double left = data[0];
        double right = data[1];
        double currentDifference = left - right;
        if (currentDifference > difference) {
            Robot.drivetrain.tankDrive(0.55, 0.5);
        } else {
            Robot.drivetrain.tankDrive(0.5, 0.55);
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
