package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.SHOOTER_ENCODER_A_CHANNEL;
import static edu.stuy.robot.RobotMap.SHOOTER_ENCODER_B_CHANNEL;
import static edu.stuy.robot.RobotMap.SHOOTER_ENCODER_MAXSPEED;
import static edu.stuy.robot.RobotMap.SHOOTER_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.SHOOTER_WHEEL_DIAMETER;
import static edu.stuy.robot.RobotMap.EPSILON;
import static java.lang.Math.PI;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {

	private CANTalon shooterMotor;
	private Encoder shooterEncoder;

	public Shooter() {
		shooterMotor = new CANTalon(SHOOTER_MOTOR_CHANNEL);
		shooterEncoder = new Encoder(SHOOTER_ENCODER_A_CHANNEL, SHOOTER_ENCODER_B_CHANNEL);
	}

	public void stop() {
		shooterMotor.set(0.0);
	}

	public int getEncoder() {
		return shooterEncoder.get();
	}

	public void setSpeed(double speed) {
		shooterMotor.set(speed);
	}

	public void setSpeedHigh() {
		shooterMotor.set(1.0);
	}

	public void setSpeedMedium() {
		shooterMotor.set(0.5);
	}

	public void setSpeedLow() {
		shooterMotor.set(0.25);
	}

	private double convertToLinearSpeed(double RPM) {
		return RPM / 60 * PI * SHOOTER_WHEEL_DIAMETER;
	}

	private double convertToAngularSpeed(double linearSpeed) {
		return linearSpeed / (SHOOTER_WHEEL_DIAMETER / 2);
	}

	private double convertAngularSpeedToMotorSpeed(double angularSpeed) {
		return angularSpeed / SHOOTER_ENCODER_MAXSPEED;
	}

	public double getCurrentMotorSpeed() {
		return convertAngularSpeedToMotorSpeed(convertToAngularSpeed(convertToLinearSpeed(shooterMotor.getSpeed())));
	}

	// Use the encoders to verify the speed
	public void setSpeedReliably(double speed) {
		double currentSpeed = speed;
		double startTime = Timer.getFPGATimestamp();
		shooterMotor.set(currentSpeed);
		while (Math.abs(getCurrentMotorSpeed() - speed) < EPSILON) {
			if (getCurrentMotorSpeed() - speed > 0.0) {
				currentSpeed -= 0.01;
			} else {
				currentSpeed += 0.01;
			}
			shooterMotor.set(currentSpeed);
			if (Timer.getFPGATimestamp() - startTime > 2000) {
				return;
			}
		}
	}

	public void setShooterBrakeMode(boolean on) {
		shooterMotor.enableBrakeMode(on);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new ShooterTestSpeed());
	}
}
