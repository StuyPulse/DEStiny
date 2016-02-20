package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class DropDownMoveToAngleCommand extends Command{
	
	private int desiredAngle;
	
	public DropDownMoveToAngleCommand(int angle) {
		desiredAngle = angle;
		requires(Robot.dropdown);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if (Robot.dropdown.getAngle() < desiredAngle) {
			Robot.dropdown.go(-0.4);
		} else {
			Robot.dropdown.go(0.55);
		}
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(Robot.dropdown.getAngle() - desiredAngle) < 4.0;
	}

	@Override
	protected void end() {
	    Robot.dropdown.go(0.0);
	}

	@Override
	protected void interrupted() {
	}
}
