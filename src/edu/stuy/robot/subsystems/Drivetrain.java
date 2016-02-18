package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.DRIVETRAIN_ENCODER_INCHES_PER_PULSE;
import static edu.stuy.robot.RobotMap.FRONT_LEFT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.FRONT_RIGHT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.GEAR_SHIFT_CHANNEL;
import static edu.stuy.robot.RobotMap.LEFT_ENCODER_CHANNEL_A;
import static edu.stuy.robot.RobotMap.LEFT_ENCODER_CHANNEL_B;
import static edu.stuy.robot.RobotMap.REAR_LEFT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.REAR_RIGHT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.RIGHT_ENCODER_CHANNEL_A;
import static edu.stuy.robot.RobotMap.RIGHT_ENCODER_CHANNEL_B;

import edu.stuy.robot.commands.DrivetrainTankDriveCommand;
import edu.stuy.util.EfficientRamper;
import edu.stuy.util.Ramper;
import edu.stuy.util.RamperTimeLoop;
import edu.stuy.util.TankDriveOutput;
import edu.stuy.util.TemporaryRampingHandler;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

	private boolean useRamping = true;
	private final short RAMP_TYPE = 0;
	/*
	 * RAMP_TYPE:
	 * 0: Ramper:                   (Complicated Graph Ramping)
	 * 1: EfficientRamping:         (Way simpler ramping)
	 * 2: TemporaryRampingHandler:  (Semi-simple, probably doesn't work)
	 */

	private TemporaryRampingHandler rampLeftOld;
	private TemporaryRampingHandler rampRightOld;
	private double rampSpeed = 0.1;

	private Ramper rampLeft;
	private Ramper rampRight;
	private EfficientRamper rampLeftEfficient;
	private EfficientRamper rampRightEfficient;
	private RamperTimeLoop rampTimeLoop;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public Drivetrain() {
		gearShift = new Solenoid(GEAR_SHIFT_CHANNEL);
		currents = new double[10];
		leftFrontMotor = new CANTalon(FRONT_LEFT_MOTOR_CHANNEL);
		rightFrontMotor = new CANTalon(FRONT_RIGHT_MOTOR_CHANNEL);
		leftRearMotor = new CANTalon(REAR_LEFT_MOTOR_CHANNEL);
		rightRearMotor = new CANTalon(REAR_RIGHT_MOTOR_CHANNEL);
		leftFrontMotor.setInverted(true);
		rightFrontMotor.setInverted(true);
		leftRearMotor.setInverted(true);
		rightRearMotor.setInverted(true);
		robotDrive = new RobotDrive(leftFrontMotor, leftRearMotor, rightFrontMotor, rightRearMotor);

		rightEncoder = new Encoder(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B);
		leftEncoder = new Encoder(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B);

		out = new TankDriveOutput(robotDrive);
		gyro = new ADXRS450_Gyro();
		pid = new PIDController(0.030, 0.010, 0.05, gyro, out);
		drifts[0] = 0.0;

		if (RAMP_TYPE == 2) {
			rampLeftOld = new TemporaryRampingHandler(0, 0, rampSpeed);
			rampRightOld = new TemporaryRampingHandler(0, 0, rampSpeed);
		} else if (RAMP_TYPE == 0) {
			rampLeft = new Ramper();
			rampRight = new Ramper();
			rampTimeLoop = new RamperTimeLoop();
			rampTimeLoop.addRamper(rampLeft);
			rampTimeLoop.addRamper(rampRight);
			rampTimeLoop.start();
		} else if (RAMP_TYPE == 1) {
			rampLeftEfficient = new EfficientRamper();
			rampRightEfficient = new EfficientRamper();
			rampTimeLoop = new RamperTimeLoop();
			rampTimeLoop.addRamper(rampLeftEfficient);
			rampTimeLoop.addRamper(rampRightEfficient);
			rampTimeLoop.start();
		}



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
		if (useRamping) {
			if (RAMP_TYPE == 2) {
				// updateRamping() COULD be moved into DrivetrainTankDriveCommand
				updateOldRamping();	
				rampLeftOld.setTarget(left);
				rampRightOld.setTarget(right);
				robotDrive.tankDrive(rampLeftOld.getValue(), rampRightOld.getValue());
			} else if (RAMP_TYPE == 0) {
				rampLeft.setTarget(left);
				rampRight.setTarget(right);
				robotDrive.tankDrive(rampLeft.getValue(), rampRight.getValue());
			} else if (RAMP_TYPE == 1) {
				rampLeftEfficient.setTarget(left);
				rampRightEfficient.setTarget(right);
				robotDrive.tankDrive(rampLeftEfficient.getValue(), rampRightEfficient.getValue());
			}
		} else {
			robotDrive.tankDrive(left, right);
		}
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

	public double getLeftEncoder() {
		return Math.abs(leftEncoder.getDistance());
	}

	public double getRightEncoder() {
		return Math.abs(rightEncoder.getDistance());
	}

	public double getDistance() {
		double left = getLeftEncoder();
		double right = getRightEncoder();
		return Math.max(left, right);
	}

	public void resetEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
	}

	public void stop() {
		robotDrive.tankDrive(0.0, 0.0);
	}

	public void autoGearShift(boolean override) {
		if (override) {
			gearCounter = 0;
			return;
		}

		if (gearCounter == 10) {
			double sum = 0;
			for (int i = 0; i < currents.length; i++) {
				sum += currents[i];
			}
			gearUp = sum / currents.length > SmartDashboard.getNumber("Gear Shifting Threshold");
			gearShift.set(gearUp);
			gearCounter = 0;
		} else {
			currents[gearCounter] = getAverageCurrent();
			gearCounter++;
		}
	}

	public void manualgearShift(boolean on) {
		gearShift.set(on);
		gearUp = on;
	}

	public double getAverageCurrent() {
		return (leftRearMotor.getOutputCurrent() + rightRearMotor.getOutputCurrent() + leftFrontMotor.getOutputCurrent()
				+ rightFrontMotor.getOutputCurrent()) / 4;
	}

	public void setDrivetrainBrakeMode(boolean on) {
		leftFrontMotor.enableBrakeMode(on);
		leftRearMotor.enableBrakeMode(on);
		rightFrontMotor.enableBrakeMode(on);
		rightRearMotor.enableBrakeMode(on);
	}

	public void updateOldRamping() {
		rampLeftOld.update();
		rampRightOld.update();
	}

	public void setRamping(boolean bool) {
		useRamping = bool;
	}

	public boolean getGearShiftState() {
		return gearUp;
	}
}
