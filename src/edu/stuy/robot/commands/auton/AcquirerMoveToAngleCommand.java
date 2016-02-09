package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class AcquirerMoveToAngleCommand extends Command{
	
	private int desiredAngle;
	
	public AcquirerMoveToAngleCommand(int angle) {
		desiredAngle = angle;
	}
	
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void execute() {
		if (Robot.acquirer.getAngle() < desiredAngle) {
			Robot.acquirer.move(0.35);
		} else {
			Robot.acquirer.move(-0.35);
		}
	}

	@Override:
	protected boolean isFinished() {
		return Math.abs(Robot.acquirer.getAngle() - desiredAngle) < 2;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
