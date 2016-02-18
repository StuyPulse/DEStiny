package edu.stuy.robot.commands;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DropDownGoCommand extends Command {

	private double speed;
	double speedFactor = 1.0;
	double startTime;
	double currentTime;

	public DropDownGoCommand() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.dropdown);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		speed = Robot.oi.operatorGamepad.getRightY() * speedFactor;
		if (speed < 0) {
			// When lift amount is negative the dropdown goes up
			Robot.dropdown.go(speed * 0.75);
		} else {
			Robot.dropdown.go(speed * 0.5);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}

	// TODO: move to separate branch
	private void calculateTime() {
		currentTime = Timer.getFPGATimestamp() - startTime;
	}

	private void ramping() {
		double x = 2 * PI * currentTime - PI / 2;
		double y = 2 * PI * currentTime + PI / 2;
		// This is to make sure that the value inside sine does not exceed 90
		// degrees
		// The print line that confirms whether or not reseting the timer works
		if (currentTime <= .5) {
			if (x > PI) {
				x = PI;
				System.out.println("ABORT");
			}
			speedFactor = .25 * sin(x) + .25;
		} else if (.5 < currentTime && currentTime <= 1) {
			if (y > PI) {
				y = PI;
				System.out.println("ABORT");
			}
			speedFactor = .25 * sin(y) + .75;
		}
	}
}
