package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.SHOOTER_ENCODER_MAXSPEED;
import static edu.stuy.robot.RobotMap.SHOOTER_MOTOR_CHANNEL;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Class for the shooter subsystem for DEStiny
 *
 * NOTES: - Setting the shooterMotor can be done in 2 modes: -
 * Shooter.setSpeed(value) sets the speed in the standard [-1.0, 1.0] range -
 * Shooter.setRMP(value) sets the RPM [-6600,6600]
 */
public class Shooter extends Subsystem {

    private CANTalon shooterMotor;

    public double currentSpeed;

    public Shooter() {
        shooterMotor = new CANTalon(SHOOTER_MOTOR_CHANNEL);
        currentSpeed = 1.0;
        /*
         * shooterMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative
         * ); shooterMotor.reverseSensor(false);
         * shooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
         * shooterMotor.configPeakOutputVoltage(+12.0f, -12.0f);
         * shooterMotor.setProfile(0); shooterMotor.setF(0.08);
         * shooterMotor.setP(0.45); shooterMotor.setI(0.1);
         * shooterMotor.setD(0.05);
         * shooterMotor.changeControlMode(TalonControlMode.Speed);
         */
    }

    public void setSpeed(double speed) {
        // shooterMotor.changeControlMode(TalonControlMode.PercentVbus);
        shooterMotor.set(speed);
        currentSpeed = speed;
    }

    /*
     * public void setRPM(double rpm) {
     * shooterMotor.changeControlMode(TalonControlMode.Speed);
     * shooterMotor.set(rpm); }
     */

    public void stop() {
        setSpeed(0.0);
    }

    /*
     * public void setSpeedHigh() { setRPM(SHOOTER_ENCODER_MAXSPEED - 400.0); }
     */
    public double getCurrentMotorSpeedInRPM() {
        return shooterMotor.getSpeed();
    }

    // Use the encoders to verify the speed
    /**
     * Sets the speed higher or lower (by 50.0) depending on the RPM
     * 
     * @param RPM
     *            - The speed of the shooter from the RPM
     */
    /*
     * public void setSpeedReliablyRPM(double RPM) { double currentRPM = RPM;
     * double startTime = Timer.getFPGATimestamp(); setRPM(currentRPM); while
     * (Math.abs(getCurrentMotorSpeedInRPM() - RPM) < 150.0) { if
     * (getCurrentMotorSpeedInRPM() - RPM > 0.0) { currentRPM -= 50.0; } else {
     * currentRPM += 50.0; } setRPM(currentRPM); if (Timer.getFPGATimestamp() -
     * startTime > 2000.0) { return; } } }
     */

    /**
     * Sets the speed higher or lower (by 0.05) depending on V Bus.
     * 
     * @param speed
     *            - The speed of the shooter from the V Bus.
     */
    public void setSpeedReliablyVBus(double speed) {
        double currentSpeed = speed;
        double startTime = Timer.getFPGATimestamp();
        setSpeed(currentSpeed);
        while (Math.abs(getCurrentMotorSpeedInRPM() - speed * SHOOTER_ENCODER_MAXSPEED) < 150.0) {
            if (getCurrentMotorSpeedInRPM() - speed * SHOOTER_ENCODER_MAXSPEED > 0.0) {
                currentSpeed -= 0.05;
            } else {
                currentSpeed += 0.05;
            }
            setSpeed(currentSpeed);
            if (Timer.getFPGATimestamp() - startTime > 2000.0) {
                return;
            }
        }
    }

    public void setShooterBrakeMode(boolean on) {
        shooterMotor.enableBrakeMode(on);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new ShooterTestSpeed());
    }
}
