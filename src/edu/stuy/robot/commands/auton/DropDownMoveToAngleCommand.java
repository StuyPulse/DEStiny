package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class DropDownMoveToAngleCommand extends Command{
	
	private double desiredAngle;
	
	public DropDownMoveToAngleCommand(double angle) {
		desiredAngle = angle;
		requires(Robot.dropdown);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if (Robot.dropdown.getAngle() < desiredAngle) {
			Robot.dropdown.move(-0.4);
		} else {
			Robot.dropdown.move(0.55);
		}
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(Robot.dropdown.getAngle() - desiredAngle) < 4.0;
	}

	@Override
	protected void end() {
	    Robot.dropdown.currentAngle = desiredAngle;
	    Robot.dropdown.move(0.0);
	}

	@Override
	protected void interrupted() {
	}
}
