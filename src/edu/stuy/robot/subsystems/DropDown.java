package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.ACQUIRER_POTENTIOMETER_CHANNEL;
import static edu.stuy.robot.RobotMap.DROPDOWN_MOTOR_CHANNEL;
import edu.stuy.robot.commands.DropDownGoCommand;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DropDown extends Subsystem {

	private CANTalon dropDownMotor;
	private Potentiometer potentiometer;
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public DropDown() {
		dropDownMotor = new CANTalon(DROPDOWN_MOTOR_CHANNEL);
		dropDownMotor.setInverted(true);
		potentiometer = new AnalogPotentiometer(ACQUIRER_POTENTIOMETER_CHANNEL, 300, 0);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new DropDownGoCommand());
	}

	public void go(double speed) {
		dropDownMotor.set(speed);
	}

	public void stop() {
		dropDownMotor.set(0.0);
	}

	public double getVoltage() {
		return potentiometer.get();
	}

	public double getAngle() {
		double x = getVoltage();
		return (x - SmartDashboard.getNumber("Initial Voltage")) *
				SmartDashboard.getNumber("Conversion Factor");
	}

	public void move(double speed) {
		dropDownMotor.set(speed);
	}

	public void lowerAcquirerToDrivingPosition() {
		dropDownMotor.set(0.25);
	}
}
