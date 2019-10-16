package frc.robot;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import stuylib.input.Gamepad;
import stuylib.input.gamepads.Logitech.XMode;
import stuylib.math.NumberDrag;
import stuylib.math.InputScaler;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private WPI_TalonSRX leftFrontMotor;
  private WPI_TalonSRX rightFrontMotor;
  private WPI_TalonSRX leftRearMotor;
  private WPI_TalonSRX rightRearMotor;
  private DifferentialDrive differentialDrive;
  private SpeedControllerGroup leftSpeedController;
  private SpeedControllerGroup rightSpeedController;

  private WPI_VictorSPX shooterMotor;

  public Gamepad driverGamepad;
  public NumberDrag shooterDrag = new NumberDrag(32);
  
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    driverGamepad = new XMode(0);

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

    shooterMotor = new WPI_VictorSPX(13);
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        break;
      case kDefaultAuto:
      default:
        break;
    }
  }

  @Override
  public void teleopPeriodic() {
    // differentialDrive.tankDrive(driverGamepad.getLeftY(), driverGamepad.getRightY());
    double newv = shooterDrag.drag(InputScaler.square(driverGamepad.getLeftY()));
    shooterMotor.set(0.08*Math.signum(newv) + 0.92*newv);
  }

  @Override
  public void testPeriodic() {
  }
}
