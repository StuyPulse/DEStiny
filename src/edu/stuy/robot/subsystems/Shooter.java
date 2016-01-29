package edu.stuy.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import static edu.stuy.robot.RobotMap.*;
/**
 *
 */
public class Discharge extends Subsystem {

	private CANTalon dischargeMotor;

	public Discharge() {
		dischargeMotor = new CANTalon(DISCHARGE_MOTOR_CHANNEL);
	}

	public void stop() {
		dischargeMotor.set(0.0);
	}

	public void setSpeed() {
		dischargeMotor.set(convertAngularSpeedtoToMotorSpeed(calculateSpeed()));
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