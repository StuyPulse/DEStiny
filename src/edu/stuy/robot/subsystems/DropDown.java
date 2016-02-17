package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.ACQUIRER_POTENTIOMETER_CHANNEL;
import static edu.stuy.robot.RobotMap.CONVERSION_FACTOR;
import static edu.stuy.robot.RobotMap.DROPDOWN_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.INITIAL_VOLTAGE;

import edu.stuy.robot.commands.DropDownGoCommand;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
//import edu.stuy.robot.commands.DropDownGoCommand;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

/**
 *
 */
public class DropDown extends Subsystem {

	private CANTalon dropDownMotor;
	private DigitalInput limitSwitch;
	private Potentiometer potentiometer;
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public DropDown() {
		dropDownMotor = new CANTalon(DROPDOWN_MOTOR_CHANNEL);
		dropDownMotor.setInverted(true);
		// limitSwitch = new DigitalInput(LIMIT_SWITCH_CHANNEL);
		potentiometer = new AnalogPotentiometer(ACQUIRER_POTENTIOMETER_CHANNEL,
				300, 0);
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
		dropDownMotor.set(0.0);
	}

	public double getVoltage() {
		return potentiometer.get();
	}

	public double getAngle() {
		double x = getVoltage();
		return (x - INITIAL_VOLTAGE) * CONVERSION_FACTOR;
	}

	public void move(double speed) {
		dropDownMotor.set(speed);
	}
}
