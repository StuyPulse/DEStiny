package edu.stuy.robot.subsystems;


import static edu.stuy.robot.RobotMap.SHOOTER_ENCODER_A_CHANNEL;
import static edu.stuy.robot.RobotMap.SHOOTER_ENCODER_B_CHANNEL;
import static edu.stuy.robot.RobotMap.SHOOTER_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.SHOOTER_WHEEL_DIAMETER;
import edu.stuy.robot.commands.ShooterTestSpeed;
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
	private double timeBefore;
	private int encoderBefore;

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

	private double calculateSpeed() {
		int difference = shooterEncoder.get() - encoderBefore;
		double timeDif = Timer.getFPGATimestamp() - timeBefore;
		return SHOOTER_WHEEL_DIAMETER * Math.PI * difference / timeDif;
	}

	private double convertAngularSpeedtoToMotorSpeed(double angularSpeed) {
		return angularSpeed;
		// Need To Do This. This is not done. DONT THINK THAT THIS IS DONE JUST
		// BECAUSE THERE IS CODE HERE
	}

	// For testing the encoder -- delete afterwards
	public void setSpeedTesting(double speed) {
		shooterMotor.set(speed);
	}

	public void setShooterBrakeMode(boolean on) {
		shooterMotor.enableBrakeMode(on);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new ShooterTestSpeed());
	}
}
