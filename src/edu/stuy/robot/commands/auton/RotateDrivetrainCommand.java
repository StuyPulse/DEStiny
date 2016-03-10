package edu.stuy.robot.commands.auton;

import static edu.stuy.robot.RobotMap.SLOT_ANGLE_TO_GOAL_2;
import static edu.stuy.robot.RobotMap.SLOT_ANGLE_TO_GOAL_3;
import static edu.stuy.robot.RobotMap.SLOT_ANGLE_TO_GOAL_4;
import static edu.stuy.robot.RobotMap.SLOT_ANGLE_TO_GOAL_5;
import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Turns robot relative to its current position
 */
public class RotateDrivetrainCommand extends Command {

    private double angle;
    private boolean angleAtRunTime;

    public RotateDrivetrainCommand(double angle) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        this.angle = angle;
    }

    public RotateDrivetrainCommand() {
        requires(Robot.drivetrain);
        angleAtRunTime = true;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.resetGyro();
        if (angleAtRunTime) {
            Integer autonPosition = (Integer) Robot.autonPositionChooser.getSelected();
            angle = correspondingAngle(autonPosition);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // right is negative when turning right
        if (angle < 0) {
            Robot.drivetrain.tankDrive(-0.5, 0.5);
        } else {
            Robot.drivetrain.tankDrive(0.5, -0.5);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (angle < 0) {
            return true; // Math.abs(Robot.drivetrain.getGyroAngle() - (360 + angle)) < 1.0;
        } else {
            return true; // Math.abs(Robot.drivetrain.getGyroAngle() - angle) < 1.0;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

    private double correspondingAngle(Integer position) {
        if (position == 2) {
            return SLOT_ANGLE_TO_GOAL_2;
        } else if (position == 3) {
            return SLOT_ANGLE_TO_GOAL_3;
        } else if (position == 4) {
            return SLOT_ANGLE_TO_GOAL_4;
        } else {
            return SLOT_ANGLE_TO_GOAL_5;
        }
    }
}
