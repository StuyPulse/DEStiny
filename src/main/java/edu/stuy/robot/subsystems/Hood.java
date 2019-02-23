package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.HOOD_SOLENOID_CHANNEL;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hood extends Subsystem {

	private Solenoid hoodSolenoid;
	private boolean up;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public Hood() {
		hoodSolenoid = new Solenoid(0, HOOD_SOLENOID_CHANNEL);
		up = true;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void changePosition(boolean up) {
		hoodSolenoid.set(up);
	    this.up = up;
	}

	public boolean getState() {
		return up;
	}
}
