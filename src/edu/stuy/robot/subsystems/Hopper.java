package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.HOPPER_MOTOR_CHANNEL;
import edu.stuy.robot.commands.HopperStopCommand;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hopper extends Subsystem {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	private CANTalon hopperMotor;

	public Hopper() {
		hopperMotor = new CANTalon(HOPPER_MOTOR_CHANNEL);
		hopperMotor.setInverted(true);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new HopperStopCommand());
	}

	public void feed() {
		hopperMotor.set(1.0);
	}

	public void stop() {
		hopperMotor.set(0.0);
	}

	public void setHopperBrakeMode(boolean on) {
		hopperMotor.enableBrakeMode(on);
	}
}