package edu.stuy.robot.commands;

import edu.stuy.robot.Robot;

public class AdjustIfNecessaryCommand extends GyroRotationalCommand {

    @Override
    protected void setDesiredAngle() {
        // Sometimes when going over an obstacle the bot rotates to
        // an odd angle. If it cannot see the goal, rotate back to
        // its original orientation
        double[] cvReading = Robot.vision.processImage();
        if (cvReading == null) {
            canProceed = true;
            // The gyro is reset in autonomousInit and not reset in
            // any auton-obstacle routines (and this must be run before
            // CV rotation).
            desiredAngle = -Robot.drivetrain.getGyroAngle();
        } else {
            canProceed = false;
        }
    }

    @Override
    protected void onEnd() {
    }

}
