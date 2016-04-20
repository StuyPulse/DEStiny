package edu.stuy.robot.commands.auton;

import edu.stuy.robot.commands.FlashlightOnCommand;
import edu.stuy.robot.commands.HighGearCommand;
import edu.stuy.robot.commands.HopperRunCommand;
import edu.stuy.robot.commands.LowGearCommand;
import edu.stuy.robot.commands.RotateToAimCommand;
import edu.stuy.robot.commands.ShooterSetOutWorksSpeed;
import edu.stuy.robot.commands.ShooterStopCommand;
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
        if (position != 3) {
            // RotateDrivetrainCommand will automatically decide angle
            // based on Robot.autonPositionChooser.getSelected()
            addSequential(new RotateDrivetrainCommand());
        }
        addParallel(new FlashlightOnCommand()); // So we can see where it is aiming
        addSequential(new RotateToAimCommand());
        addSequential(new DriveStraightWithSonarCommand(40.0), 5.0);
        addSequential(new ShooterSetOutWorksSpeed());
        addSequential(new HighGearCommand());
        addSequential(new ShooterStopCommand());
    }
}
