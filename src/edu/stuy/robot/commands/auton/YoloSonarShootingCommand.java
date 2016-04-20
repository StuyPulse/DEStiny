package edu.stuy.robot.commands.auton;

import static edu.stuy.robot.RobotMap.SHOOTER_LAYUP_SPEED;
import edu.stuy.robot.commands.HopperStopCommand;
import edu.stuy.robot.commands.ShooterStopCommand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class YoloSonarShootingCommand extends CommandGroup {
    // This is for last resort shooting from position three if CV does not work
    // and we are desperate for shooting in auton

    private static final int DISTANCE_FROM_TOWER = 76;

    public YoloSonarShootingCommand(Command command) {
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
        addSequential(command);
        addSequential(new DropDownMoveToAngleCommand(0));
        addSequential(new DriveStraightWithSonarCommand(DISTANCE_FROM_TOWER), 5.0);
        addSequential(new ShootWithTimeoutCommand(2, 1.0));
        addSequential(new AutonHopperCommand(2.0));
        addSequential(new ShooterStopCommand());
        addSequential(new HopperStopCommand());
    }
}
