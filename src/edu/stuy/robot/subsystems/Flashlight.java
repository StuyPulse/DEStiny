package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.FLASHLIGHT_CHANNEL;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Flashlight extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private DigitalOutput flashlight;
    private boolean on;

    public Flashlight() {
        flashlight = new DigitalOutput(FLASHLIGHT_CHANNEL);
        flashlightOff();
    }

    public void flashlightOn() {
        flashlight.set(true);
        on = true;
    }

    public void flashlightOff() {
        flashlight.set(false);
        on = false;
    }

    public void flashlightToggle() {
        if (on) {
            flashlightOff();
        } else {
            flashlightOn();
        }
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
