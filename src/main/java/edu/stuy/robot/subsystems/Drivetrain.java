package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.DRIVETRAIN_ENCODER_INCHES_PER_PULSE;
import static edu.stuy.robot.RobotMap.FRONT_LEFT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.FRONT_RIGHT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.GEAR_SHIFT_CHANNEL;
import static edu.stuy.robot.RobotMap.LEFT_ENCODER_CHANNEL_A;
import static edu.stuy.robot.RobotMap.LEFT_ENCODER_CHANNEL_B;
import static edu.stuy.robot.RobotMap.MAX_DEGREES_OFF_AUTO_AIMING;
import static edu.stuy.robot.RobotMap.REAR_LEFT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.REAR_RIGHT_MOTOR_CHANNEL;
import static edu.stuy.robot.RobotMap.RIGHT_ENCODER_CHANNEL_A;
import static edu.stuy.robot.RobotMap.RIGHT_ENCODER_CHANNEL_B;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.stuy.robot.commands.DrivetrainTankDriveCommand;
import edu.stuy.util.TankDriveOutput;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drivetrain extends Subsystem {
    private Encoder rightEncoder;
    private Encoder leftEncoder;
    private WPI_TalonSRX leftFrontMotor;
    private WPI_TalonSRX rightFrontMotor;
    private WPI_TalonSRX leftRearMotor;
    private WPI_TalonSRX rightRearMotor;
    private DifferentialDrive differentialDrive;
    private ADXRS450_Gyro gyro;
    private TankDriveOutput out;
    private Solenoid gearShift;
    private double[] currents;
    private SpeedControllerGroup leftSpeedController;
    private SpeedControllerGroup rightSpeedController;
    public final PIDController pid;

    public boolean gearUp; // Stores the state of the gear shift
    public boolean overrideAutoGearShifting; // True if automatic gear shifting is not being used
    public boolean autoGearShiftingState; // True if automatic gear shifting was disabled and never re-enabled

    private int gearCounter = 0;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public Drivetrain() {
        gearShift = new Solenoid(GEAR_SHIFT_CHANNEL);
        currents = new double[10];
        leftFrontMotor = new WPI_TalonSRX(FRONT_LEFT_MOTOR_CHANNEL);
        rightFrontMotor = new WPI_TalonSRX(FRONT_RIGHT_MOTOR_CHANNEL);
        leftRearMotor = new WPI_TalonSRX(REAR_LEFT_MOTOR_CHANNEL);
        rightRearMotor = new WPI_TalonSRX(REAR_RIGHT_MOTOR_CHANNEL);
        leftFrontMotor.setInverted(true);
        rightFrontMotor.setInverted(true);
        leftRearMotor.setInverted(true);
        rightRearMotor.setInverted(true);
        leftSpeedController = new SpeedControllerGroup(leftFrontMotor, leftRearMotor);
        rightSpeedController = new SpeedControllerGroup(rightFrontMotor, rightRearMotor);
        differentialDrive = new DifferentialDrive(leftSpeedController, rightSpeedController);

        rightEncoder = new Encoder(RIGHT_ENCODER_CHANNEL_A,
                RIGHT_ENCODER_CHANNEL_B);
        leftEncoder = new Encoder(LEFT_ENCODER_CHANNEL_A,
                LEFT_ENCODER_CHANNEL_B);

        gearUp = false;
        overrideAutoGearShifting = false;
        autoGearShiftingState = true;

        // Setup PIDController for auto-rotation and aiming
        out = new TankDriveOutput(differentialDrive);
        gyro = new ADXRS450_Gyro();
        pid = new PIDController(SmartDashboard.getNumber("Gyro P", 0),
                SmartDashboard.getNumber("Gyro I", 0),
                SmartDashboard.getNumber("Gyro D", 0), gyro, out);
        pid.setInputRange(0, 360);
        pid.setContinuous(); // Tell `pid' that 0deg = 360deg
        pid.setAbsoluteTolerance(MAX_DEGREES_OFF_AUTO_AIMING);

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
        differentialDrive.tankDrive(left, right);
    }

     public double getGyroAngle() {
        return gyro.getAngle();
    }

    public double getLeftEncoderAbs() {
        return Math.abs(leftEncoder.getDistance());
    }

    public double getRightEncoderAbs() {
        return Math.abs(rightEncoder.getDistance());
    }

    public double getLeftEncoder() {
        return leftEncoder.getDistance();
    }

    public double getRightEncoder() {
        return rightEncoder.getDistance();
    }

    public double getDistance() {
        double left = getLeftEncoderAbs();
        double right = getRightEncoderAbs();
        return Math.max(left, right);
    }

    public double getDisplacement() {
        double left = getLeftEncoder();
        double right = getRightEncoder();
        return (left + right) / 2;
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void stop() {
        differentialDrive.tankDrive(0.0, 0.0);
    }

    /**
     * Changes from high gear to low gear or low gear to high gear automatically when the current spikes
     * @return Changes from high gear to low gear or low gear to high gear automatically when the current spikes
     */
    public void autoGearShift() {
        if (overrideAutoGearShifting) {
            gearCounter = 0;
            return;
        }

        if (gearCounter == 10) {
            double sum = 0;
            for (int i = 0; i < currents.length; i++) {
                sum += currents[i];
            }
            gearUp = sum / currents.length > SmartDashboard
                    .getNumber("Gear Shifting Threshold", 0);
            gearShift.set(gearUp);
            gearCounter = 0;
        } else {
            currents[gearCounter] = getAverageCurrent();
            gearCounter++;
        }
    }
    
    /**
     * Forces a gear shift, regardless of automatic gear shifting.
     * @param on - True if low gear is desired. False if high gear is desired.
     */
    public void manualGearShift(boolean on) {
        gearShift.set(on);
        gearUp = on;
    }

    /**
     * @param input - The joystick value
     * @return input^2 if input is positive, -(input^2) if input is negative.
     */
    public double inputSquared(double input) {
        double retVal = input;
        retVal = retVal * retVal;
        if (input < 0) {
            retVal *= -1;
        }
        return retVal;
    }

    /**
     * @return The average currents from all 4 motors to help with automatic gear shifting.
     */
    public double getAverageCurrent() {
        return (leftRearMotor.getOutputCurrent()
                + rightRearMotor.getOutputCurrent()
                + leftFrontMotor.getOutputCurrent()
                + rightFrontMotor.getOutputCurrent()) / 4;
    }

    public void setDrivetrainBrakeMode(boolean on) {
        leftFrontMotor.setNeutralMode(NeutralMode.Brake);
        leftRearMotor.setNeutralMode(NeutralMode.Brake);
        rightFrontMotor.setNeutralMode(NeutralMode.Brake);
        rightRearMotor.setNeutralMode(NeutralMode.Brake);
    }

    public void resetGyro() {
        gyro.reset();
    }
}
