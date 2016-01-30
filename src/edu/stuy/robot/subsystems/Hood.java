package edu.stuy.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import static edu.stuy.robot.RobotMap.PISTON_CHANNEL;
/**
 *
 */
public class Hood extends Subsystem {
    private Solenoid hoodSolenoid;
    private boolean up;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public Hood() {
    	hoodSolenoid = new Solenoid(PISTON_CHANNEL);
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
    	up = x;
    }
}

