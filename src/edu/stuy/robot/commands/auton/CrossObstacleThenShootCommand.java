package edu.stuy.robot.commands.auton;

import static edu.stuy.robot.RobotMap.DISTANCE_TO_WALL;
import edu.stuy.robot.commands.HopperRunCommand;
import edu.stuy.robot.commands.SetupforShotCommand;
import edu.stuy.robot.commands.ShooterSetOutWorksSpeed;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CrossObstacleThenShootCommand extends CommandGroup {

    public CrossObstacleThenShootCommand(Command obstacle, Integer position) {
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
        //Turns robot to face wall
        addSequential(new AlignWithWallCommand(0.5));
        //Sets robot a specific distance away from the wall
        addSequential(new SetDistanceFromWallCommand(DISTANCE_TO_WALL, 0.5));
        addSequential(new RotateDrivetrainCommand());
        // TODO: Fix RotateDrivetrainCommand to work once we have PID tuning
        addSequential(new SetupforShotCommand());
        addSequential(new ShooterSetOutWorksSpeed());
        addSequential(new HopperRunCommand(true));
    }
}
