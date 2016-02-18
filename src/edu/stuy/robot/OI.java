package edu.stuy.robot;

import static edu.stuy.robot.RobotMap.DRIVER_GAMEPAD;
import static edu.stuy.robot.RobotMap.OPERATOR_GAMEPAD;
import edu.stuy.robot.commands.AcquirerAcquireCommand;
import edu.stuy.robot.commands.AcquirerDeacquireCommand;
import edu.stuy.robot.commands.DisableAutoGearShiftCommand;
import edu.stuy.robot.commands.EnableAutoGearShiftCommand;
import edu.stuy.robot.commands.HoodToggleCommand;
import edu.stuy.robot.commands.HopperFeedCommand;
import edu.stuy.robot.commands.JionDriveCommand;
import edu.stuy.robot.commands.ShooterToggleCommand;
import edu.stuy.robot.commands.auton.DropDownMoveToAngleCommand;
import edu.stuy.util.Gamepad;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	public Gamepad driverGamepad;
	public Gamepad operatorGamepad;

	public OI() {
		driverGamepad = new Gamepad(DRIVER_GAMEPAD);
		operatorGamepad = new Gamepad(OPERATOR_GAMEPAD);

		driverGamepad.getStartButton().whenPressed(new EnableAutoGearShiftCommand());
		driverGamepad.getSelectButton().whenPressed(new DisableAutoGearShiftCommand());
		driverGamepad.getLeftTrigger().whileHeld(new JionDriveCommand());

		//operatorGamepad.getBottomButton().whileHeld(new SetupforShotCommand());
		operatorGamepad.getLeftTrigger().whileHeld(new HopperFeedCommand());
		operatorGamepad.getLeftBumper().whileHeld(new HopperFeedCommand());
		operatorGamepad.getRightTrigger().whileHeld(new AcquirerAcquireCommand());
		operatorGamepad.getRightBumper().whileHeld(new AcquirerDeacquireCommand());
		operatorGamepad.getDPadLeft().whenPressed(new ShooterToggleCommand());
		operatorGamepad.getDPadUp().whenPressed(new ShooterToggleCommand());
		operatorGamepad.getDPadRight().whenPressed(new ShooterToggleCommand());
		operatorGamepad.getDPadDown().whenPressed(new ShooterToggleCommand());
		operatorGamepad.getTopButton().whenPressed(new HoodToggleCommand());
		operatorGamepad.getRightAnalogButton().whenPressed(new DropDownMoveToAngleCommand(35));
	}
}
