package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.ACQUIRER_MOTOR_CHANNEL;
import edu.stuy.robot.commands.AcquirerStopCommand;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Acquirer extends Subsystem {

	private CANTalon acquirerMotor;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public Acquirer() {
		acquirerMotor = new CANTalon(ACQUIRER_MOTOR_CHANNEL);
	}

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