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
			Robot.dropdown.move(0.35);
		} else {
			Robot.dropdown.move(-0.35);
		}
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(Robot.dropdown.getAngle() - desiredAngle) < 2;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}
}
