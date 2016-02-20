package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.ACQUIRER_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.ACQUIRER_POTENTIOMETER_CHANNEL;
import edu.stuy.robot.commands.AcquirerStopCommand;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

/**
 *
 */
public class Acquirer extends Subsystem {

	private CANTalon acquirerMotor;
	private String outString = "";
	private final static double factor = 1.0;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

 	public Acquirer() {
		acquirerMotor = new CANTalon(ACQUIRER_MOTOR_CHANNEL);
	}

	// Used for auton

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new AcquirerStopCommand());
	}

	public void acquire() {
		acquirerMotor.set(1.0);
	}

	public void stop() {
		acquirerMotor.set(0.0);
	}

	public void deacquire() {
		acquirerMotor.set(-1.0);
	}
}