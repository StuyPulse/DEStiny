package edu.stuy.robot.commands;
import static java.lang.Math.PI;
import static java.lang.Math.sin;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
/**
 *
 */
public class DropDownGoCommand extends Command {

	private double liftamount;
	double speedFactor;
	double startTime;
	double currentTime;

    public DropDownGoCommand() {
        // Use requires() here to declare subsystem dependencies
    	requires(Robot.dropdown);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        calculateTime();
        accelerate();
    	liftamount = Robot.oi.operatorGamepad.getLeftY() * speedFactor;
    	Robot.dropdown.go(liftamount);
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

	private void calculateTime () {
		currentTime = Timer.getFPGATimestamp() - startTime;
	}

	private void accelerate() {
		if (currentTime <= .5) {
			speedFactor = .25*sin(2*PI*currentTime-PI/2)+.25;
		} else if (.5 < currentTime && currentTime <= 1) {
			speedFactor = .25*sin(2*PI*currentTime+PI/2)+.75;
		}
	}
}
