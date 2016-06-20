package edu.stuy.robot.commands.auton;

import edu.stuy.robot.commands.AdjustIfNecessaryCommand;
import edu.stuy.robot.commands.DrivetrainStopCommand;
import edu.stuy.robot.commands.FlashlightOnCommand;
import edu.stuy.robot.commands.LowGearCommand;
import edu.stuy.robot.commands.SetupForShotCommand;
import edu.stuy.robot.commands.ShooterSetLayupCommand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CrossObstacleThenShootCommand extends CommandGroup {

    public CrossObstacleThenShootCommand(Command obstacle, int position) {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        // addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        // addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.

        addSequential(obstacle);
        addSequential(new LowGearCommand());
        addSequential(new DrivetrainStopCommand());
        addSequential(new AdjustIfNecessaryCommand());
        addParallel(new ShooterSetLayupCommand());
        addSequential(new DropDownMoveToAngleCommand(0), 2.0);
        if (position != 3 && position != 4) {
            // RotateDrivetrainCommand will, at runtime, decide angle
            // based on Robot.autonPositionChooser.getSelected()
            addSequential(new RotateDrivetrainCommand());
        }
        addParallel(new FlashlightOnCommand()); // So we can see where it is aiming
        addSequential(new SetupForShotCommand());

        // The following only runs the hopper if goal was in frame for CV
        addSequential(new HandleAutonShotCommand(), 3.0);

        // Drivetrain returns to high gear in teleopInit, where
        // also shooter, hopper and flashlight are turned off.
    }
}
