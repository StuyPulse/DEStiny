package edu.stuy.robot.commands.auton;

import edu.stuy.robot.commands.HoodDownCommand;
import edu.stuy.robot.commands.SetupforShotCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootFromSpyAndMoveCommand extends CommandGroup {
    
    public  ShootFromSpyAndMoveCommand() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
        addSequential(new SetupforShotCommand());
        addSequential(new ShootOuterworkCommand());
        addSequential(new HoodDownCommand());
        addSequential(new RotateDrivetrainCommand(60.0));
        addSequential(new DriveForwardCommand(40, 5, 0.75));
    }
}
