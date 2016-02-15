package edu.stuy.robot.subsystems;

import edu.stuy.robot.commands.FeederStopCommand;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import static edu.stuy.robot.RobotMap.*;
/**
 *
 */
public class Feeder extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANTalon feederMotor;
    
    public Feeder() {
    	feederMotor = new CANTalon(FEEDER_MOTOR_CHANNEL);
    }
  
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new FeederStopCommand());
    }
    
    public void stop() {
    	feederMotor.set(0.0);
    }
    
    public void feed() {
		feederMotor.set(0.75);
	}
}