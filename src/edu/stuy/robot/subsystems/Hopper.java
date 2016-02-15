package edu.stuy.robot.subsystems;

import edu.stuy.robot.commands.HopperStopCommand;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import static edu.stuy.robot.RobotMap.*;

/**
 *
 */
public class Hopper extends Subsystem {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	private CANTalon hopperMotor;

	public Hopper() {
		hopperMotor = new CANTalon(HOPPER_MOTOR_CHANNEL);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new HopperStopCommand());
	}

	public void stop() {
		hopperMotor.set(0.0);
	}

	public void feed() {
		hopperMotor.set(0.75);
	}
}