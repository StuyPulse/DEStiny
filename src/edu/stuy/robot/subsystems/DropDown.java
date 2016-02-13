package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.DROPDOWN_MOTOR_CHANNEL;

//import edu.stuy.robot.commands.DropDownGoCommand;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DropDown extends Subsystem {
    private CANTalon dropDownMotor;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public DropDown () {
    	dropDownMotor = new CANTalon(DROPDOWN_MOTOR_CHANNEL);
    }


    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new DropDownGoCommand());
    }

    public void go(double liftvalue) {
    	dropDownMotor.set(liftvalue);
    }
}

