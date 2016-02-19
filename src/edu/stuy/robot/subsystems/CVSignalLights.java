package edu.stuy.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

import static edu.stuy.robot.RobotMap.CV_SIGNAL_LIGHT_RED_PORT;
import static edu.stuy.robot.RobotMap.CV_SIGNAL_LIGHT_YELLOW_PORT;
import static edu.stuy.robot.RobotMap.CV_SIGNAL_LIGHT_BLUE_PORT;

public class CVSignalLights extends Subsystem {

    private DigitalOutput redLight;
    private DigitalOutput yellowLight;
    private DigitalOutput blueLight;
    
    public CVSignalLights() {
        redLight = new DigitalOutput(CV_SIGNAL_LIGHT_RED_PORT);
        yellowLight = new DigitalOutput(CV_SIGNAL_LIGHT_YELLOW_PORT);
        blueLight = new DigitalOutput(CV_SIGNAL_LIGHT_BLUE_PORT);
    }

    public void initDefaultCommand() {
        setLights(false, false, false);
    }

    public void setNotInFrame() {
        setLights(true, false, false);
    }

    public void setInFrame() {
        setLights(false, true, false);
    }

    public void setReadyToShoot() {
        setLights(false, false, true);
    }

    public void turnOffAll() {
    	setLights(false, false, false);
    }

    private void setLights(
            boolean red,
            boolean yellow,
            boolean blue) {
        redLight.set(red);
        yellowLight.set(yellow);
        blueLight.set(blue);
    }
}
