package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.SIGNAL_LIGHT_RED_PORT;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class RedSignalLight extends Subsystem {

    private DigitalOutput redLight;

    public void initDefaultCommand() {
        redLight = new DigitalOutput(SIGNAL_LIGHT_RED_PORT);
    }

    public void set(boolean on) {
        redLight.set(on);
    }

    public void setOn() {
        set(true);
    }

    public void setOff() {
        set(false);
    }
}

