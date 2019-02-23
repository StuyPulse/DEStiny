package edu.stuy.util;

import static edu.stuy.robot.RobotMap.PID_MAX_ROBOT_SPEED;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class TankDriveOutput implements PIDOutput {

	private DifferentialDrive pidDrive;
	private double maxValue;

	public TankDriveOutput(DifferentialDrive drive) {
		pidDrive = drive;
		maxValue = PID_MAX_ROBOT_SPEED;
	}

	@Override
	public void pidWrite(double output) {
		if (Math.abs(output) > maxValue) {
			output = maxValue * (output / Math.abs(output));
		}
		pidDrive.tankDrive(output, -output);
	}
}
