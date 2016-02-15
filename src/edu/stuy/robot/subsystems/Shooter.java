package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.SHOOTER_ENCODER_B_CHANNEL;
import static edu.stuy.robot.RobotMap.SHOOTER_ENCODER_A_CHANNEL;
import static edu.stuy.robot.RobotMap.SHOOTER_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.WHEEL_DIAMETER;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {

	private CANTalon shooterMotor;
	private Encoder enc;
	private double timeBefore;
	private int encoderBefore;

	public Shooter() {
		shooterMotor = new CANTalon(SHOOTER_MOTOR_CHANNEL);
		enc = new Encoder(SHOOTER_ENCODER_A_CHANNEL, SHOOTER_ENCODER_B_CHANNEL);
	}

	public void stop() {
		shooterMotor.set(0.0);
	}

	public int getEncoder() {
		return enc.get();
	}

	public void setSpeed() {
		encoderBefore = enc.get();
		timeBefore = Timer.getFPGATimestamp();
		shooterMotor.set(convertAngularSpeedtoToMotorSpeed(calculateSpeed()));
	}

	private double calculateSpeed() {
		int difference = enc.get() - encoderBefore;
		double timeDif = Timer.getFPGATimestamp() - timeBefore;
		return WHEEL_DIAMETER * Math.PI * difference / timeDif;
	}

	private double convertAngularSpeedtoToMotorSpeed(double angularSpeed) {
		return angularSpeed;
		// Need To Do This. This is not done. DONT THINK THAT THIS IS DONE JUST
		// BECAUSE THERE IS CODE HERE
	}

	//For testing the encoder -- delete afterwards
	//public void setSpeedTesting(double speed) {
	//	shooterMotor.set(speed);
	//}
	

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
	}
}
