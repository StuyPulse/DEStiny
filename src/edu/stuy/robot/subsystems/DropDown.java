package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.ACQUIRER_POTENTIOMETER_CHANNEL;
import static edu.stuy.robot.RobotMap.DROPDOWN_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.DROP_DOWN_DEADBAND;
import edu.stuy.robot.commands.DropDownDefaultCommand;
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
    public double currentAngle;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public DropDown() {
        dropDownMotor = new CANTalon(DROPDOWN_MOTOR_CHANNEL);
        dropDownMotor.setInverted(true);
        potentiometer = new AnalogPotentiometer(ACQUIRER_POTENTIOMETER_CHANNEL,
                300, 0);
        currentAngle = getAngle();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DropDownDefaultCommand());
    }

    public void move(double speed) {
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
        double initialVoltage = 152.0;
        double finalVoltage = 224.4;
        double conversionFactor = 90.0 / (finalVoltage - initialVoltage);
        return (x - initialVoltage) * conversionFactor;
    }

    /**
     * Use in auton to lower acquirer to driving position
     */
    public void lowerAcquirerToDrivingPosition() {
        dropDownMotor.set(0.25);
    }

    public void setDropDownBreakMode(boolean breakMode) {
        dropDownMotor.enableBrakeMode(breakMode);
    }

    /**
     * Returns any number and its absolute value less than 0.1, to zero.
     * 
     * @param speed
     *            - The speed of the dropdown
     * @return Any number and its absolute value less than 0.1, to zero.
     */

    public boolean deadband(double speed) {
        return Math.abs(speed) < DROP_DOWN_DEADBAND;
    }
}
