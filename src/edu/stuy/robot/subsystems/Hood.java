package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.EXTRA_HOOD_SOLENOID_CHANNEL;
import static edu.stuy.robot.RobotMap.HOOD_SOLENOID_CHANNEL;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
/**
 *
 */
public class Hood extends Subsystem {

    private Solenoid hoodSolenoid;
    private Solenoid extraHoodSolenoid;
    private boolean up;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public Hood() {
    	hoodSolenoid = new Solenoid(HOOD_SOLENOID_CHANNEL);
    	extraHoodSolenoid = new Solenoid(EXTRA_HOOD_SOLENOID_CHANNEL);
    	up = false;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void toggle() {
    	if (up) {
    		changePosition(false);
    	}
    	else {
    		changePosition(true);
    	}
    }
    
    public void changePosition(boolean x) {
    	hoodSolenoid.set(x);
    	extraHoodSolenoid.set(x);
    	up = x;
    }
}

