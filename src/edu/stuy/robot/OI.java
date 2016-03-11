package edu.stuy.robot;

import static edu.stuy.robot.RobotMap.DRIVER_GAMEPAD;
import static edu.stuy.robot.RobotMap.OPERATOR_GAMEPAD;

import edu.stuy.robot.commands.AcquirerAcquireCommand;
import edu.stuy.robot.commands.AcquirerDeacquireCommand;
import edu.stuy.robot.commands.DisableAutoGearShiftCommand;
import edu.stuy.robot.commands.EnableAutoGearShiftCommand;
import edu.stuy.robot.commands.FlashlightToggleCommand;
import edu.stuy.robot.commands.HighGearCommand;
import edu.stuy.robot.commands.HoodDownCommand;
import edu.stuy.robot.commands.HoodUpCommand;
import edu.stuy.robot.commands.HopperRunCommand;
import edu.stuy.robot.commands.JionDriveCommand;
import edu.stuy.robot.commands.MoveIntoShotRangeCommand;
import edu.stuy.robot.commands.RotateToGoalCommand;
import edu.stuy.robot.commands.ShooterBackwardsCommand;
import edu.stuy.robot.commands.ShooterSetLayupCommand;
import edu.stuy.robot.commands.ShooterSetMaxSpeed;
import edu.stuy.robot.commands.ShooterSetOutWorksSpeed;
import edu.stuy.robot.commands.ShooterStopCommand;
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

        // DRIVER BINDINGS
        driverGamepad.getStartButton().whenPressed(new EnableAutoGearShiftCommand());
        driverGamepad.getSelectButton().whenPressed(new DisableAutoGearShiftCommand());
        driverGamepad.getLeftTrigger().whileHeld(new JionDriveCommand());
        driverGamepad.getLeftTrigger().whenReleased(new HighGearCommand());
        driverGamepad.getRightBumper().whenPressed(new FlashlightToggleCommand());
        driverGamepad.getLeftBumper().whenPressed(new FlashlightToggleCommand());

        driverGamepad.getBottomButton().whenPressed(new RotateToGoalCommand());
        driverGamepad.getLeftButton().whenPressed(new MoveIntoShotRangeCommand());

        // OPERATOR BINDINGS
        operatorGamepad.getLeftTrigger().whileHeld(new HopperRunCommand(true));
        operatorGamepad.getLeftBumper().whileHeld(new HopperRunCommand(false));
        operatorGamepad.getRightTrigger().whileHeld(new AcquirerAcquireCommand());
        operatorGamepad.getRightBumper().whileHeld(new AcquirerDeacquireCommand());

        operatorGamepad.getDPadUp().whenPressed(new ShooterSetOutWorksSpeed());
        operatorGamepad.getDPadLeft().whenPressed(new ShooterSetLayupCommand());
        operatorGamepad.getDPadRight().whenPressed(new ShooterSetMaxSpeed());
        operatorGamepad.getDPadDown().whenPressed(new ShooterStopCommand());

        operatorGamepad.getLeftButton().whenPressed(new ShooterBackwardsCommand());
        operatorGamepad.getLeftButton().whenReleased(new ShooterStopCommand());
        operatorGamepad.getTopButton().whenPressed(new HoodUpCommand());
        operatorGamepad.getRightButton().whenPressed(new HoodDownCommand());

        operatorGamepad.getRightAnalogButton().whenPressed(new DropDownMoveToAngleCommand(40));
    }
}
