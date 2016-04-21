package edu.stuy.robot.commands;

import java.util.Arrays;

import edu.stuy.robot.Robot;
import edu.stuy.robot.cv.StuyVision;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RotateToAimPIDCommand extends RotateDegreesPIDCommand {

    public RotateToAimPIDCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super();
    }

    private void sprintSetup() {
        Robot.drivetrain.resetGyro();

        long start = System.currentTimeMillis();
        double[] cvReading = Robot.vision.processImage();
        System.out.println("Image processing took " + (System.currentTimeMillis() - start) + "ms");

        boolean goalInFrame = cvReading != null;
        if (goalInFrame) {
            this.targetAngle = StuyVision.frameXPxToDegrees(cvReading[0]);
            System.out.println("Reading was: " + Arrays.toString(cvReading) + "-----------------------------");
            System.out.println("Desired Angle Delta: " + targetAngle);
        } else {
            System.out.println("Reading was NULL------------------------------------------------------------");
        }
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        sprintSetup();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
