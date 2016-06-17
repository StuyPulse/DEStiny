package edu.stuy.robot;

import static edu.stuy.robot.RobotMap.HOOD_DOWN_POSITION;
import static edu.stuy.robot.RobotMap.JONAH_ID;
import static edu.stuy.robot.RobotMap.SHOOTER_SPEED_LABEL;
import static edu.stuy.robot.RobotMap.SIGNAL_LIGHT_BLUE_PORT;
import static edu.stuy.robot.RobotMap.SIGNAL_LIGHT_YELLOW_PORT;
import static edu.stuy.robot.RobotMap.YUBIN_ID;

import edu.stuy.robot.commands.auton.CrossObstacleThenShootCommand;
import edu.stuy.robot.commands.auton.GoOverMoatCommand;
import edu.stuy.robot.commands.auton.GoOverRampartsCommand;
import edu.stuy.robot.commands.auton.GoOverRockWallBackwardsCommand;
import edu.stuy.robot.commands.auton.GoOverRockWallCommand;
import edu.stuy.robot.commands.auton.GoOverRoughTerrainCommand;
import edu.stuy.robot.commands.auton.PassChevalCommand;
import edu.stuy.robot.commands.auton.PassPortcullisCommand;
import edu.stuy.robot.commands.auton.ReachObstacleCommand;
import edu.stuy.robot.cv.StuyVision;
import edu.stuy.robot.subsystems.Acquirer;
import edu.stuy.robot.subsystems.Drivetrain;
import edu.stuy.robot.subsystems.DropDown;
import edu.stuy.robot.subsystems.Flashlight;
import edu.stuy.robot.subsystems.Hood;
import edu.stuy.robot.subsystems.Hopper;
import edu.stuy.robot.subsystems.Shooter;
import edu.stuy.robot.subsystems.Sonar;
import edu.stuy.util.BoolBox;
import edu.stuy.util.SignalLight;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    public static Hopper hopper;
    public static Drivetrain drivetrain;
    public static Acquirer acquirer;
    public static DropDown dropdown;
    public static Shooter shooter;
    public static Hood hood;
    public static Sonar sonar;
    public static SignalLight blueSignalLight;
    public static SignalLight cvSignalLight;
    public static Flashlight flashlight;
    public static OI oi;

    Command selectedAutonomousCommand;
    Command autonomousCommand;

    public static SendableChooser debugChooser;
    public static SendableChooser autonChooser;
    public static SendableChooser operatorChooser;
    public static SendableChooser autonPositionChooser;
    public static SendableChooser autonShootChooser;

    private double autonStartTime;
    private static boolean debugMode;

    public static StuyVision vision;
    public static BoolBox stopAutoMovement = new BoolBox(false);

    // Used to not waste a ball in a shooting auton if CV doesn't
    // find the goal (because, e.g., the bot got rekt by obstacle):
    public static boolean cvFoundGoal = false;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        setupDebugChooser();
        debugMode = (Boolean) debugChooser.getSelected();

        // GyroPID
        SmartDashboard.putNumber("Gyro P", 0);
        SmartDashboard.putNumber("Gyro I", 0);
        SmartDashboard.putNumber("Gyro D", 0);

        // Potentiometer
        double initialVoltage = 78;
        double finalVoltage = 145;
        SmartDashboard.putNumber("Initial Voltage", initialVoltage);
        SmartDashboard.putNumber("Final Voltage", finalVoltage);
        SmartDashboard.putNumber("Conversion Factor", 90.0 / (finalVoltage - initialVoltage));

        // Angle to move in gyro predetermined-angle (non-CV) auto-rotation
        SmartDashboard.putNumber("gyro-rotate-degs", 90);

        // Start the operator chooser before anything else
        chooseOperator();

        // Initialize all the subsystems
        drivetrain = new Drivetrain();
        acquirer = new Acquirer();
        dropdown = new DropDown();
        hopper = new Hopper();
        shooter = new Shooter();
        hood = new Hood();
        sonar = new Sonar();
        blueSignalLight = new SignalLight(SIGNAL_LIGHT_BLUE_PORT, true);
        cvSignalLight = new SignalLight(SIGNAL_LIGHT_YELLOW_PORT, true);

        // Turn off the cv signal light
        cvSignalLight.stayOff();

        flashlight = new Flashlight();

        oi = new OI();
        vision = new StuyVision();

        drivetrain.setDrivetrainBrakeMode(true);
        shooter.setShooterBrakeMode(false);
        hopper.setHopperBrakeMode(true);
        dropdown.setDropDownBreakMode(true);

        SmartDashboard.putNumber(SHOOTER_SPEED_LABEL, 0.0);

        // Auton Distances:
        SmartDashboard.putNumber("Rock", 0);
        SmartDashboard.putNumber("Moat", 0);
        SmartDashboard.putNumber("Rough", 0);
        SmartDashboard.putNumber("Ramparts", 0);
        SmartDashboard.putNumber("Draw", 0); // complex
        SmartDashboard.putNumber("Cheval", 0);
        SmartDashboard.putNumber("Portcullis", 0); // complex

        drivetrain.setDrivetrainBrakeMode(true);
        shooter.setShooterBrakeMode(false);
        hopper.setHopperBrakeMode(true);
        dropdown.setDropDownBreakMode(true);

        // Set up the auton chooser
        setupAutonChooser();
        setupAutonPositionChooser();
        setupShootChooser();
    }

    /**
     * Maps the relative locations of the obstacles
     */
    private void setupAutonPositionChooser() {
        autonPositionChooser = new SendableChooser();
        SmartDashboard.putString("1", "The low bar is in position 1, not a valid autonomous choice");
        autonPositionChooser.addObject("2", 2);
        autonPositionChooser.addDefault("3", 3);
        autonPositionChooser.addObject("4", 4);
        autonPositionChooser.addObject("5", 5);
        SmartDashboard.putData("Auton Position", autonPositionChooser);
    }

    private void chooseOperator() {
        operatorChooser = new SendableChooser();
        operatorChooser.addDefault("Jonah", JONAH_ID);
        operatorChooser.addObject("Yubin", YUBIN_ID);
        SmartDashboard.putData("Choose thy Operator Knight!", operatorChooser);
    }

    private void setupAutonChooser() {
        autonChooser = new SendableChooser();
        autonChooser.addDefault("0. Reach edge of obstacle but refrain from going over", new ReachObstacleCommand());
        autonChooser.addObject("1. Do nothing", new CommandGroup());
        autonChooser.addObject("2. Rock Wall", new GoOverRockWallCommand());
        autonChooser.addObject("3. Moat", new GoOverMoatCommand());
        autonChooser.addObject("4. Rough Terrain", new GoOverRoughTerrainCommand());
        autonChooser.addObject("5. Ramparts", new GoOverRampartsCommand());
        autonChooser.addObject("6. Cheval", new PassChevalCommand());
        autonChooser.addObject("7. Portcullis", new PassPortcullisCommand());
        autonChooser.addObject("8. Rock Back", new GoOverRockWallBackwardsCommand());
        SmartDashboard.putData("Auton setting", autonChooser);
    }

    private void setupDebugChooser() {
        debugChooser = new SendableChooser();
        debugChooser.addDefault("Competition Mode", false);
        debugChooser.addObject("Debug Mode", true);
        SmartDashboard.putData("SmartDashboard Mode", debugChooser);
    }

    private void setupShootChooser() {
        autonShootChooser = new SendableChooser();
        autonShootChooser.addDefault("Do not shoot in auton", false);
        autonShootChooser.addObject("Do shoot in auton", true);
        SmartDashboard.putData("Auton Shooting", autonShootChooser);
    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
        try {
            Robot.cvSignalLight.stayOff();
            debugMode = (Boolean) debugChooser.getSelected();
            Command selected = (Command) autonChooser.getSelected();
            if (selected != selectedAutonomousCommand) {
                int autonPosition = (Integer) autonPositionChooser.getSelected();
                boolean shoot = (Boolean) autonShootChooser.getSelected();
                selectedAutonomousCommand = selected;
                if (shoot) {
                    autonomousCommand = new CrossObstacleThenShootCommand(selectedAutonomousCommand, autonPosition);
                } else {
                    autonomousCommand = selectedAutonomousCommand;
                }
            }

            autonomousCommand.start();
            Robot.drivetrain.resetEncoders();
            autonStartTime = Timer.getFPGATimestamp();
        } catch (Exception e) {
            System.err.println("\n\n\n\n\nTOP-LEVEL CATCH in autonomousInit. Exception was:");
            e.printStackTrace();
            System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        }
    }

    public void autonomousPeriodic() {
        try {
            Scheduler.getInstance().run();
            if (debugMode) {
                SmartDashboard.putNumber("drivetrain left encoder", Robot.drivetrain.getLeftEncoderAbs());
                SmartDashboard.putNumber("drivetrain right encoder", Robot.drivetrain.getRightEncoderAbs());
                SmartDashboard.putNumber("Max distance of drivetrain encoders", Robot.drivetrain.getDistance());
                SmartDashboard.putNumber("potentiometer", Robot.dropdown.getAngle());
                SmartDashboard.putNumber("Potentiometer voltage", Robot.dropdown.getVoltage());
            }
            Robot.blueSignalLight.tick();
            // This block is here instead of teleop init to save battery voltage
            // because teleop init does not run immediately after auton disables
            if (Timer.getFPGATimestamp() - autonStartTime > 14) {
                Robot.shooter.stop();
                Robot.hopper.stop();
            }
        } catch (Exception e) {
            System.err.println("\n\n\n\n\nTOP-LEVEL CATCH in autonomousPeriodic. Exception was:");
            e.printStackTrace();
            System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        }
    }

    public void teleopInit() {
        try {
            // This makes sure that the autonomous stops running when
            // teleop starts running. If you want the autonomous to
            // continue until interrupted by another command, remove
            // this line or comment it out.
            if (autonomousCommand != null) {
                autonomousCommand.cancel();
            }
            debugMode = (Boolean) debugChooser.getSelected();

            Robot.drivetrain.resetEncoders();

            // Shooter, hopper stopped also after a timeout in autonomus periodic as a safety measure
            Robot.shooter.stop();
            Robot.hopper.stop();

            Robot.flashlight.flashlightOff();
            Robot.hood.changePosition(HOOD_DOWN_POSITION);
            Robot.drivetrain.manualGearShift(false);
        } catch (Exception e) {
            System.err.println("\n\n\n\n\nTOP-LEVEL CATCH in telopInit. Exception was:");
            e.printStackTrace();
            System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        }
    }

    /**
     * This function is called when the disabled button is hit. You can use it
     * to reset subsystems before shutting down.
     */
    public void disabledInit() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        try {
            Scheduler.getInstance().run();
            Robot.blueSignalLight.tick();
            if (debugMode) {
                SmartDashboard.putNumber("gyro", Robot.drivetrain.getGyroAngle());
                SmartDashboard.putNumber("Current Shooter Motor Speed:", Robot.shooter.getCurrentMotorSpeedInRPM());
                SmartDashboard.putNumber("drivetrain left encoder", Robot.drivetrain.getLeftEncoderAbs());
                SmartDashboard.putNumber("drivetrain right encoder", Robot.drivetrain.getRightEncoderAbs());
                SmartDashboard.putBoolean("Gear shift override", drivetrain.overrideAutoGearShifting);
                try {
                    SmartDashboard.putNumber("Hopper Sensor", Robot.hopper.getDistance());
                } catch (Exception e) {
                    SmartDashboard.putNumber("Hopper Sensor", -1.0);
                }
                // Sonar:
                double[] sonarData = sonar.getData();
                SmartDashboard.putNumber("Sonar L", sonarData[0]);
                SmartDashboard.putNumber("Sonar R", sonarData[1]);

                // Solenoids:
                SmartDashboard.putBoolean("Hood piston", Robot.hood.getState());
                SmartDashboard.putBoolean("Gear shift solenoid", Robot.drivetrain.gearUp);

                // Thresholds:
                SmartDashboard.putNumber("Gear Shifting Threshold", 40);
            }
            SmartDashboard.putNumber("potentiometer", Robot.dropdown.getAngle());
            SmartDashboard.putNumber("Potentiometer voltage", Robot.dropdown.getVoltage());
        } catch (Exception e) {
            System.err.println("\n\n\n\n\nTOP-LEVEL CATCH in teleopPeriodic. Exception was:");
            e.printStackTrace();
            System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        }
        try {
            hopper.runHopperSensor();
        } catch (Exception e) {
            System.err.println("Hopper sensor failed");
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }

    public static boolean isDebugModeOn() {
        return debugMode;
    }
}
