package edu.stuy.robot.commands.auton;

import static edu.stuy.robot.RobotMap.SLOT_ANGLE_TO_GOAL_2;
import static edu.stuy.robot.RobotMap.SLOT_ANGLE_TO_GOAL_3;
import static edu.stuy.robot.RobotMap.SLOT_ANGLE_TO_GOAL_4;
import static edu.stuy.robot.RobotMap.SLOT_ANGLE_TO_GOAL_5;

import edu.stuy.robot.Robot;
import edu.stuy.robot.commands.GyroRotationalCommand;

/**
 * Turns robot relative to its current position
 */
public class RotateDrivetrainCommand extends GyroRotationalCommand {

    @Override
    protected void setDesiredAngle() {
        int position = (Integer) Robot.autonPositionChooser.getSelected();
        if (position == 2) {
            desiredAngle = SLOT_ANGLE_TO_GOAL_2;
        } else if (position == 3) {
            desiredAngle = SLOT_ANGLE_TO_GOAL_3;
        } else if (position == 4) {
            desiredAngle = SLOT_ANGLE_TO_GOAL_4;
        } else if (position == 5) {
            desiredAngle = SLOT_ANGLE_TO_GOAL_5;
        } else {
            desiredAngle = 0.0;
        }
    }
}
