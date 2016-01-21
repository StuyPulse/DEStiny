package edu.stuy.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import static edu.stuy.robot.RobotMap.SHOOTER_MOTOR_CHANNEL;

/**
 *
 */
public class Shooter extends Subsystem {
	private CANTalon shooterMotor;

	public Shooter() {
		shooterMotor = new CANTalon(SHOOTER_MOTOR_CHANNEL);
	}

	public void stop() {
		shooterMotor.set(0.0);
	}

	public void setSpeed() {
		shooterMotor.set(convertAngularSpeedtoToMotorSpeed(calculateSpeed()));
	}

	private double calculateSpeed() {
		return 0.0;
	}

	private double convertAngularSpeedtoToMotorSpeed(double angularSpeed) {
		return 0.0;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
