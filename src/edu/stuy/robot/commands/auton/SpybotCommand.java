package edu.stuy.robot.commands.auton;

import edu.stuy.robot.commands.HoodDownCommand;
import edu.stuy.robot.commands.HoodUpCommand;
import edu.stuy.robot.commands.HopperStopCommand;
import edu.stuy.robot.commands.ShooterSetMaxSpeed;
import edu.stuy.robot.commands.ShooterStopCommand;
import edu.stuy.robot.commands.auton.DropDownMoveToAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class SpybotCommand extends CommandGroup {
    
    public  SpybotCommand() {
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
        
        addSequential(new HoodUpCommand());
        addSequential(new DropDownMoveToAngleCommand(0);
        addParallel(new ShooterSetMaxSpeed(), 4);
        addSequential(new AutonHopperCommand(4.0));
        addParallel(new ShooterStopCommand());
        addSequential(new HopperStopCommand());
        addSequential(new HoodDownCommand());
    }
}
