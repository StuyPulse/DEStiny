package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.DRIVETRAIN_ENCODER_INCHES_PER_PULSE;
import static edu.stuy.robot.RobotMap.FRONT_LEFT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.FRONT_RIGHT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.LEFT_ENCODER_CHANNEL_B;
import static edu.stuy.robot.RobotMap.LEFT_ENCODER_CHANNEL_A;
import static edu.stuy.robot.RobotMap.REAR_LEFT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.REAR_RIGHT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.RIGHT_ENCODER_CHANNEL_B;
import static edu.stuy.robot.RobotMap.RIGHT_ENCODER_CHANNEL_A;
import static edu.stuy.robot.RobotMap.GEAR_SHIFT_CHANNEL;
import static edu.stuy.robot.RobotMap.GEAR_SHIFT_THRESHOLD;
import edu.stuy.robot.commands.DrivetrainTankDriveCommand;
import edu.stuy.util.TankDriveOutput;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem {
	private Encoder rightEncoder;
	private Encoder leftEncoder;
	private CANTalon leftFrontMotor;
	private CANTalon rightFrontMotor;
	private CANTalon leftRearMotor;
	private CANTalon rightRearMotor;
	private RobotDrive robotDrive;
	private ADXRS450_Gyro gyro;
	private PIDController pid;
	private TankDriveOutput out;
	private Solenoid gearShift;
	private boolean gearUp;
	private double[] currents;

	private int gearCounter = 0;
	private double[] drifts = new double[8];
	private int counter = 0;
	private double currentAngle = 0.0;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public Drivetrain() {
		gearShift = new Solenoid(GEAR_SHIFT_CHANNEL);
		currents = new double[10];
		leftFrontMotor = new CANTalon(FRONT_LEFT_MOTOR_CHANNEL);
		rightFrontMotor = new CANTalon(FRONT_RIGHT_MOTOR_CHANNEL);
		leftRearMotor = new CANTalon(REAR_LEFT_MOTOR_CHANNEL);
		rightRearMotor = new CANTalon(REAR_RIGHT_MOTOR_CHANNEL);
		robotDrive = new RobotDrive(leftFrontMotor, leftRearMotor,
				rightFrontMotor, rightRearMotor);

		rightEncoder = new Encoder(RIGHT_ENCODER_CHANNEL_A,
				RIGHT_ENCODER_CHANNEL_B);
		leftEncoder = new Encoder(LEFT_ENCODER_CHANNEL_A,
				LEFT_ENCODER_CHANNEL_B);

		out = new TankDriveOutput(robotDrive);
		gyro = new ADXRS450_Gyro();
		pid = new PIDController(0.030, 0.010, 0.05, gyro, out);
		drifts[0] = 0.0;

		// pid.setInputRange(0, 360);
		// pid.setContinuous();
		leftEncoder.setDistancePerPulse(DRIVETRAIN_ENCODER_INCHES_PER_PULSE);
		rightEncoder.setDistancePerPulse(DRIVETRAIN_ENCODER_INCHES_PER_PULSE);
		gyro.reset();
		gyro.setPIDSourceType(PIDSourceType.kDisplacement);
		gyro.calibrate();
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new DrivetrainTankDriveCommand());
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

	// side = the side it's on
	// Should work. Test it.
	public double getDistance() {
		double left = leftEncoder.getDistance();
		double right = rightEncoder.getDistance();
		return Math.max(left, right);
	}

	public void resetEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
	}

	public void stop() {
		robotDrive.tankDrive(0, 0);
	}

	public void autoGearShift() {
		if (gearCounter == 10) {
			double sum = 0;
			for (int i = 0; i == currents.length; i++) {
				sum += currents[i];
			}
			gearUp = sum / currents.length > GEAR_SHIFT_THRESHOLD;
			gearShift.set(gearUp);
			gearCounter = 0;
		} else {
			currents[gearCounter] = getAverageCurrent();
			gearCounter++;
		}
	}

	public void manualgearShift(boolean gearUp) {
		gearShift.set(gearUp);
	}

	public double getAverageCurrent() {
		return leftRearMotor.getOutputCurrent() +
				rightRearMotor.getOutputCurrent() +
				leftFrontMotor.getOutputCurrent() +
				rightFrontMotor.getOutputCurrent();
	}
}
