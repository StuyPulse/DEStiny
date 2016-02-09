
package edu.stuy.robot;

import edu.stuy.robot.commands.auton.GoOverMoatCommand;
import edu.stuy.robot.commands.auton.GoOverRampartsCommand;
import edu.stuy.robot.commands.auton.GoOverRockWallCommand;
import edu.stuy.robot.commands.auton.GoOverRoughTerrainCommand;
import edu.stuy.robot.commands.auton.PassChevalCommand;
import edu.stuy.robot.commands.auton.PassDrawbridgeCommand;
import edu.stuy.robot.commands.auton.PassPortcullisCommand;
import edu.stuy.robot.commands.auton.ReachObstacleCommand;
import edu.stuy.robot.subsystems.Acquirer;
import edu.stuy.robot.subsystems.Drivetrain;
import edu.stuy.robot.subsystems.DropDown;
import edu.stuy.robot.subsystems.Feeder;
import edu.stuy.robot.subsystems.Hood;
import edu.stuy.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
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

	public static Feeder feeder;
	public static Drivetrain drivetrain;
	public static Acquirer acquirer;
	public static Hood hood;
	public static DropDown dropdown;
	public static Shooter shooter;
	public static OI oi;
	Command autonomousCommand;
	SendableChooser autonChooser;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		drivetrain = new Drivetrain();
		acquirer = new Acquirer();
		dropdown = new DropDown();
		feeder = new Feeder();
		hood = new Hood();
		shooter = new Shooter();
		oi = new OI();
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		// schedule the autonomous command (example)
		autonomousCommand = (Command) autonChooser.getSelected();
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	private void setupAutonChooser() {
		autonChooser = new SendableChooser();
		autonChooser.addDefault("0. Do nothing", new CommandGroup());
		autonChooser.addObject("1. Reach edge of obstacle but refrain from going over", new ReachObstacleCommand());
		autonChooser.addObject("2. Rock Wall", new GoOverRockWallCommand());
		autonChooser.addObject("3. Moat", new GoOverMoatCommand());
		autonChooser.addObject("4. Rough Terrain", new GoOverRoughTerrainCommand());
		autonChooser.addObject("5. Ramparts", new GoOverRampartsCommand());
		autonChooser.addObject("6. Drawbridge", new PassDrawbridgeCommand());
		autonChooser.addObject("7. Cheval", new PassChevalCommand());
		autonChooser.addObject("8. Portcullis", new PassPortcullisCommand());
		SmartDashboard.putData("Auton setting", autonChooser);

	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
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
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("gyro", Robot.drivetrain.getGyroAngle());
		SmartDashboard.putNumber("potentiometer", Robot.acquirer.getVoltage());
		SmartDashboard.putNumber("angle", Robot.acquirer.getAngle());
		SmartDashboard.putNumber("encoder", Robot.shooter.getEncoder());
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}
}
