package edu.stuy.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import static edu.stuy.robot.RobotMap.*;

import edu.stuy.robot.commands.DrivetrainTankDriveCommand;

/**
 *
 */
public class Drivetrain extends Subsystem {
    
	private CANTalon leftFrontMotor;
	private CANTalon rightFrontMotor;
	private CANTalon leftRearMotor;
	private CANTalon rightRearMotor;
	private RobotDrive robotDrive;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public Drivetrain() {
		leftFrontMotor = new CANTalon(FRONT_LEFT_MOTOR);
		rightFrontMotor = new CANTalon(FRONT_RIGHT_MOTOR);
		leftRearMotor = new CANTalon(REAR_LEFT_MOTOR);
		rightRearMotor = new CANTalon(REAR_RIGHT_MOTOR);
		robotDrive = new RobotDrive(leftFrontMotor, leftRearMotor, rightFrontMotor, rightRearMotor);
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new DrivetrainTankDriveCommand());
    }
    public void tankDrive(double left, double right) {
    	robotDrive.tankDrive(left, right);
    }
}

