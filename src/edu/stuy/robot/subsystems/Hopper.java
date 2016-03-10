package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.HOPPER_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.HOPPER_SENSOR_CHANNEL;
import static edu.stuy.robot.RobotMap.HOPPER_SENSOR_THRESHOLD;
import edu.stuy.robot.commands.HopperStopCommand;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hopper extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANTalon hopperMotor;
    private AnalogInput distanceSensor;
    private DigitalOutput yellowStrip;

    public Hopper() {
        hopperMotor = new CANTalon(HOPPER_MOTOR_CHANNEL);
        hopperMotor.setInverted(true);
        distanceSensor = new AnalogInput(HOPPER_SENSOR_CHANNEL);
        yellowStrip = new DigitalOutput(5);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new HopperStopCommand());
    }

    public void feed() {
        hopperMotor.set(1.0);
    }

    public void vomit() {
        hopperMotor.set(-1.0);
    }

    public void stop() {
        hopperMotor.set(0.0);
    }

    public void setHopperBrakeMode(boolean on) {
        hopperMotor.enableBrakeMode(on);
    }

    public double getDistance() {
        return distanceSensor.getVoltage();
    }

    public boolean hasBall() {
        return getDistance() > HOPPER_SENSOR_THRESHOLD;
    }

    public void lightOn() {
        yellowStrip.set(true);
    }

    public void lightOff() {
        yellowStrip.set(false);
    }

    public void runHopperSensor() {
        if (hasBall()) {
            lightOn();
        } else {
            lightOff();
        }
    }
}