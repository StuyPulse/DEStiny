package edu.stuy.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import static edu.stuy.robot.RobotMap.ACQUIRER_MOTOR_CHANNEL;
/**
 *
 */
public class Acquirer extends Subsystem {

	private CANTalon acquirerMotor;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public Acquirer() {
		acquirerMotor = new CANTalon(ACQUIRER_MOTOR_CHANNEL);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

