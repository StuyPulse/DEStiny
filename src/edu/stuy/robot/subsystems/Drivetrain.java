package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.FRONT_LEFT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.FRONT_RIGHT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.REAR_LEFT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.REAR_RIGHT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.GYRO_P;
import static edu.stuy.robot.RobotMap.GYRO_I;
import static edu.stuy.robot.RobotMap.GYRO_D;

import edu.stuy.util.TankDriveOutput;
import edu.stuy.robot.commands.DrivetrainTankDriveCommand;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem {

	private CANTalon leftFrontMotor;
	private CANTalon rightFrontMotor;
	private CANTalon leftRearMotor;
	private CANTalon rightRearMotor;
	private RobotDrive robotDrive;
	private ADXRS450_Gyro gyro;
	private PIDController pid;
	private TankDriveOutput out;

	private double[] drifts = new double[8];
	private int counter = 0;
	private double currentAngle = 0.0;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public Drivetrain() {
		leftFrontMotor = new CANTalon(FRONT_LEFT_MOTOR_CHANNEL);
		rightFrontMotor = new CANTalon(FRONT_RIGHT_MOTOR_CHANNEL);
		leftRearMotor = new CANTalon(REAR_LEFT_MOTOR_CHANNEL);
		rightRearMotor = new CANTalon(REAR_RIGHT_MOTOR_CHANNEL);
		robotDrive = new RobotDrive(leftFrontMotor, leftRearMotor, rightFrontMotor, rightRearMotor);

		out = new TankDriveOutput(robotDrive);
		gyro = new ADXRS450_Gyro();
		pid = new PIDController(0.030, 0.010, 0.05, gyro, out);
		drifts[0] = 0.0;

		// pid.setInputRange(0, 360);
		// pid.setContinuous();
		gyro.reset();
		gyro.setPIDSourceType(PIDSourceType.kDisplacement);
		gyro.calibrate();
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new DrivetrainTankDriveCommand());
    	pid.enable();
    }

    public void tankDrive(double left, double right) {
    	robotDrive.tankDrive(left, right);
    }
    
    public double getGyroAngle() {
    	drifts[counter % 8] = currentAngle - gyro.getAngle();
    	currentAngle = gyro.getAngle();
    	if (counter > 9) {
    		double avg = 0.0;
    		for (double d : drifts) {
    			avg += d;
    		}
    		System.out.println("Average of 8: " + (avg / 8));
    	}
    	counter++;
    	return gyro.getAngle();
    }
    
    public double getDistance() {
    	//TODO Write the code for Encoder
    	return -1;
    }
    
    public void stop() {
    	robotDrive.tankDrive(0, 0);
    }
}
