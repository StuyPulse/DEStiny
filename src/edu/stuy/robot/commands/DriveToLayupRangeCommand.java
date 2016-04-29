package edu.stuy.robot.commands;

import static edu.stuy.robot.RobotMap.LAYUP_SHOOTING_DISTANCE;

import edu.stuy.robot.Robot;
import edu.stuy.robot.cv.StuyVision;

/**
 *
 */
public class DriveToLayupRangeCommand extends EncoderDrivingCommand {

    public DriveToLayupRangeCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    protected void setInchesToMove() {
        double[] cvReading = Robot.vision.processImage();
        if (cvReading != null) {
            double curDistance = StuyVision.findBotDistanceToGoal(cvReading[1]);
            initialInchesToMove = curDistance - LAYUP_SHOOTING_DISTANCE;
        } else {
            // CV failed!
            cancelCommand = true;
        }
    }
}
