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
import edu.stuy.util.TankDriveOutput;
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
    private double[] currents;

    public boolean gearUp; // Stores the state of the gear shift
    public boolean overrideAutoGearShifting; // True if automatic gear shifting
                                             // is not being used
    public boolean autoGearShiftingState; // True if automatic gear shifting was
                                          // disabled and never re-enabled

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
        leftFrontMotor.setInverted(true);
        rightFrontMotor.setInverted(true);
        leftRearMotor.setInverted(true);
        rightRearMotor.setInverted(true);
        robotDrive = new RobotDrive(leftFrontMotor, leftRearMotor,
                rightFrontMotor, rightRearMotor);

        rightEncoder = new Encoder(RIGHT_ENCODER_CHANNEL_A,
                RIGHT_ENCODER_CHANNEL_B);
        leftEncoder = new Encoder(LEFT_ENCODER_CHANNEL_A,
                LEFT_ENCODER_CHANNEL_B);

        gearUp = false;
        overrideAutoGearShifting = false;
        autoGearShiftingState = true;

        out = new TankDriveOutput(robotDrive);
        gyro = new ADXRS450_Gyro();
        pid = new PIDController(SmartDashboard.getNumber("Gyro P"),
                SmartDashboard.getNumber("Gyro I"),
                SmartDashboard.getNumber("Gyro D"), gyro, out);
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

    /*
     Does not work, TODO: FIX
         public double getGyroAngle() { 
             drifts[counter % 8] = currentAngle - gyro.getAngle();
             currentAngle = gyro.getAngle();
             if (counter > 9) {
                 double avg = 0.0;
                 for (double d : drifts) { avg += d; }
             }
             counter++;
             return gyro.getAngle();
         }
     */

    public double getLeftEncoder() {
        try {
            return Math.abs(leftEncoder.getDistance());
        } catch (Exception e) {
            return -1.0;
        }
    }

    public double getRightEncoder() {
        try {
            return Math.abs(rightEncoder.getDistance());
        } catch (Exception e) {
            return -1.0;
        }
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

    /**
     * Changes from high gear to low gear or low gear to high gear automatically
     * when the current spikes
     * 
     * @return Changes from high gear to low gear or low gear to high gear
     *         automatically when the current spikes
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
                    .getNumber("Gear Shifting Threshold");
            gearShift.set(gearUp);
            gearCounter = 0;
        } else {
            currents[gearCounter] = getAverageCurrent();
            gearCounter++;
        }
    }

    /**
     * Forces a gear shift, regardless of automatic gear shifting.
     * 
     * @param on
     *            - True if low gear is desired. False if high gear is desired.
     */
    public void manualGearShift(boolean on) {
        gearShift.set(on);
        gearUp = on;
    }

    /**
     * @param input
     *            - The joystick value
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
     * @return The average currents from all 4 motors to help with automatic
     *         gear shifting.
     */
    public double getAverageCurrent() {
        return (leftRearMotor.getOutputCurrent()
                + rightRearMotor.getOutputCurrent()
                + leftFrontMotor.getOutputCurrent() + rightFrontMotor
                    .getOutputCurrent()) / 4;
    }

    public void setDrivetrainBrakeMode(boolean on) {
        leftFrontMotor.enableBrakeMode(on);
        leftRearMotor.enableBrakeMode(on);
        rightFrontMotor.enableBrakeMode(on);
        rightRearMotor.enableBrakeMode(on);
    }

    public void resetGyro() {
        gyro.reset();
    }
}
