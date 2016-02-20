package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.stuy.robot.subsystems.Sonar;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AllignWithWallCommand extends Command {
	
	double turnSpeed;
	int direction;

    public AllignWithWallCommand(double turnSpeed) {
        requires(Robot.sonar);
        requires(Robot.drivetrain);
        this.turnSpeed = turnSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	direction = Robot.sonar.getSideToTurn();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	direction = Robot.sonar.getSideToTurn();
    	Robot.drivetrain.tankDrive(-1 * direction * turnSpeed, direction * turnSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (direction == 0);
    }

    // Called once after isFinished returns true
    protected void end() {
    	// Just to make sure it stops when we finish
    	Robot.drivetrain.tankDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
