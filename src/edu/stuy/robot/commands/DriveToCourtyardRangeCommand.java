package edu.stuy.robot.commands;

import static edu.stuy.robot.RobotMap.COURTYARD_SHOOTING_DISTANCE;

import edu.stuy.robot.Robot;
import edu.stuy.robot.cv.StuyVision;

/**
 *
 */
public class DriveToCourtyardRangeCommand extends EncoderDrivingCommand {

    public DriveToCourtyardRangeCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    protected void setInchesToMove() {
        double[] cvReading = Robot.vision.processImage();
        if (cvReading != null) {
            double curDistance = StuyVision.findBotDistanceToGoal(cvReading[1]);
            initialInchesToMove = curDistance - COURTYARD_SHOOTING_DISTANCE;
        } else {
            // CV failed!
            cancelCommand = true;
        }
    }
}
