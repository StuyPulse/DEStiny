/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private WPI_TalonSRX leftFrontMotor;
  private WPI_TalonSRX rightFrontMotor;
  private WPI_TalonSRX leftRearMotor;
  private WPI_TalonSRX rightRearMotor;

  private DifferentialDrive differentialDrive;

  private SpeedControllerGroup leftSpeedController;
  private SpeedControllerGroup rightSpeedController;

  private Encoder leftEncoder;
  private Encoder rightEncoder;

  public Drivetrain(){
    leftFrontMotor = new WPI_TalonSRX(1);
    rightFrontMotor = new WPI_TalonSRX(3);
    leftRearMotor = new WPI_TalonSRX(4);
    rightRearMotor = new WPI_TalonSRX(2);

    leftFrontMotor.setInverted(true);
    rightFrontMotor.setInverted(true);
    leftRearMotor.setInverted(true);
    rightRearMotor.setInverted(true);

    leftSpeedController = new SpeedControllerGroup(leftFrontMotor, leftRearMotor);
    rightSpeedController = new SpeedControllerGroup(rightFrontMotor, rightRearMotor);

    differentialDrive = new DifferentialDrive(leftSpeedController, rightSpeedController);

    leftEncoder = new Encoder(0, 1);
    rightEncoder = new Encoder(2, 3);

    leftEncoder.setDistancePerPulse(Math.PI * 7.5 / 360);
    rightEncoder.setDistancePerPulse(Math.PI * 7.5 / 360);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  
  public void tankDrive(double left, double right){
    differentialDrive.tankDrive(left, right);
  }

  public double getSpeedFromEncoder(){
    // No need for fancy math; we are 
    return (leftEncoder.getRate() + rightEncoder.getRate())/2;
  }

  
}
