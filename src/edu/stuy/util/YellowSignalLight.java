package edu.stuy.util;

import static edu.stuy.robot.RobotMap.SIGNAL_LIGHT_YELLOW_PORT;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 *
 */
public class YellowSignalLight {

    private DigitalOutput light;

    public YellowSignalLight() {
        light = new DigitalOutput(SIGNAL_LIGHT_YELLOW_PORT);
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

