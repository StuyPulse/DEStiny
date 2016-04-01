package edu.stuy.robot.commands.auton;

import edu.stuy.robot.commands.HighGearCommand;
import edu.stuy.robot.commands.HopperRunCommand;
import edu.stuy.robot.commands.LowGearCommand;
import edu.stuy.robot.commands.RotateToAimCommand;
import edu.stuy.robot.commands.ShooterSetOutWorksSpeed;
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
        if (position != 3 && position != 4) {
            // RotateDrivetrainCommand will automatically decide angle
            // based on Robot.autonPositionChooser.getSelected()
            addSequential(new RotateDrivetrainCommand());
        }
        addSequential(new ShooterSetOutWorksSpeed());
        addSequential(new RotateToAimCommand());
        addSequential(new HopperRunCommand(true));
        addSequential(new HighGearCommand());
    }
}
