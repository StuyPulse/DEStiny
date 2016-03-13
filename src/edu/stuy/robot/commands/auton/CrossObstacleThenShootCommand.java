package edu.stuy.robot.commands.auton;

import edu.stuy.robot.commands.HopperRunCommand;
import edu.stuy.robot.commands.RotateTillGoalInFrameCommand;
import edu.stuy.robot.commands.RotateToGoalCommand;
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

        // Turns robot to face wall
        //addSequential(new AlignWithWallCommand(0.5));
        // Sets robot a specific distance away from the wall
        //addSequential(new SetDistanceFromWallCommand(DISTANCE_TO_WALL, 0.5));
        // TODO: Fix RotateDrivetrainCommand to work once we have PID tuning
        //addSequential(new RotateDrivetrainCommand());

        // Rotate in a direction until goal is in frame, looking
        // right if in positions 1, 2 or 3 and left for 4 and 5
        addSequential(new RotateTillGoalInFrameCommand(position <= 3));
        addSequential(new ShooterSetOutWorksSpeed());
        addSequential(new RotateToGoalCommand());
        addSequential(new HopperRunCommand(true));
    }
}
