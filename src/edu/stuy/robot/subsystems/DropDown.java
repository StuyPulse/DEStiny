package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.DROPDOWN_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.DIGITAL_INPUT_CHANNEL;

import edu.stuy.robot.commands.DropDownGoCommand;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DropDown extends Subsystem {
	private CANTalon dropDownMotor;
	private DigitalInput limitSwitch;
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public DropDown() {
		dropDownMotor = new CANTalon(DROPDOWN_MOTOR_CHANNEL);
		limitSwitch = new DigitalInput(DIGITAL_INPUT_CHANNEL);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new DropDownGoCommand());
	}

	public void go(double speed) {
		if (limitSwitch.get() && speed < 0.0) {
			stop();
		} else {
			dropDownMotor.set(speed);
		}
    }

	public void stop() {
		// TODO: Add deceleration
		dropDownMotor.set(0.0);
	}
}
