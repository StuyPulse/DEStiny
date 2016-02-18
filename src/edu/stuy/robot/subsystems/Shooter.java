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
 * NOTES:
 *  - Setting the shooterMotor can be done in 2 modes:
 *      - Shooter.setSpeed(value) sets the speed in the standard [-1.0, 1.0] range
 *      - Shooter.setRMP(value) sets the RPM [-6600,6600]
 */
public class Shooter extends Subsystem {

    private CANTalon shooterMotor;

    public Shooter() {
        shooterMotor = new CANTalon(SHOOTER_MOTOR_CHANNEL);
        shooterMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        shooterMotor.reverseSensor(false);
        shooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
        shooterMotor.configNominalOutputVoltage(+12.0f, -12.0f);
        shooterMotor.changeControlMode(TalonControlMode.Speed);
    }

    public void setSpeed(double speed) {
        shooterMotor.changeControlMode(TalonControlMode.PercentVbus);
        shooterMotor.set(speed);
    }

    public void setRPM(double rpm) {
        shooterMotor.changeControlMode(TalonControlMode.Speed);
        shooterMotor.set(rpm);
    }

    public void stop() {
        setSpeed(0.0);
    }

    public void setSpeedHigh() {
        setRPM(SHOOTER_ENCODER_MAXSPEED);
    }

    public double getCurrentMotorSpeedInRPM() {
        return shooterMotor.getSpeed();
    }

    // Use the encoders to verify the speed
    public void setSpeedReliablyRPM(double RPM) {
        double currentRPM = RPM;
        double startTime = Timer.getFPGATimestamp();
        setRPM(currentRPM);
        while (Math.abs(getCurrentMotorSpeedInRPM() - RPM) < 150.0) {
            if (getCurrentMotorSpeedInRPM() - RPM > 0.0) {
                currentRPM -= 50.0;
            } else {
                currentRPM += 50.0;
            }
            setRPM(currentRPM);
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
