package edu.stuy.util;

import static edu.stuy.robot.RobotMap.SIGNAL_LIGHT_BLUE_PORT;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BlueSignalLight {

    private DigitalOutput light;

    public BlueSignalLight() {
        light = new DigitalOutput(SIGNAL_LIGHT_BLUE_PORT);
    }

    public void set(boolean on) {
        light.set(on);
    }

    public void setOn() {
        set(true);
    }

    public void setOff() {
        set(false);
    }
}

