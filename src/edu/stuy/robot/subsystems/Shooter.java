package edu.stuy.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import static edu.stuy.robot.RobotMap.*;
/**
 *
 */
public class Shooter extends Subsystem {

	private CANTalon shooterMotor;
    private Encoder enc;
    private double timeBefore;
    private double timeNow;
    
	public Shooter() {
		shooterMotor = new CANTalon(SHOOTER_MOTOR_CHANNEL);
		enc = new Encoder(ENCODER_CHANNEL);
	}

	public void stop() {
		shooterMotor.set(0.0);
	}

	public void setSpeed() {
	    encoderBefore = enc.get();
	    timeBefore = Timer.getFPGATimestamp();
		shooterMotor.set(convertAngularSpeedtoToMotorSpeed(calculateSpeed()));
	}

	private double calculateSpeed() {
	     int difference = enc.get() - encoderBefore;
	     double timeDif = Timer.getFPGATimestamp() - timeBefore;
	     return WHEEL_DIAMETER * Math.PI * difference / timedif;
	}

	private double convertAngularSpeedtoToMotorSpeed(double angularSpeed) {
		return angularSpeed;
		// Need To Do This. This is not done. DONT THINK THAT THIS IS DONE JUST BECAUSE THERE IS CODE HERE
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}