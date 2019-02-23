package edu.stuy.robot.commands;

import static edu.stuy.robot.RobotMap.JONAH_ID;
import static java.lang.Math.PI;
import static java.lang.Math.sin;
import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DropDownMoveCommand extends Command {

    private double speed;
    double speedFactor;
    double startTime;
    double currentTime;

    public DropDownMoveCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.dropdown);
        speedFactor = 1.0;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        speed = Robot.oi.operatorGamepad.getRightY() * speedFactor;
        int operator = (Integer) Robot.operatorChooser.getSelected();
        if (operator == JONAH_ID) {
            // Jonah plays Flight Simulators so pulling up goes down
            speed *= -1;
        }
        if (Robot.dropdown.deadband(speed)) {
            Robot.dropdown.move(0.0);
        } else if (speed < 0) {
            // When lift amount is negative the dropdown goes up
            Robot.dropdown.move(speed * 0.75);
            Robot.dropdown.currentAngle = Robot.dropdown.getAngle();
        } else {
            Robot.dropdown.move(speed * 0.35);
            Robot.dropdown.currentAngle = Robot.dropdown.getAngle();
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

    // TODO: move to separate branch
    private void calculateTime() {
        currentTime = Timer.getFPGATimestamp() - startTime;
    }

    private void ramping() {
        double x = 2 * PI * currentTime - PI / 2;
        double y = 2 * PI * currentTime + PI / 2;
        // This is to make sure that the value inside sine does not exceed 90
        // degrees
        // The print line that confirms whether or not reseting the timer works
        if (currentTime <= .5) {
            if (x > PI) {
                x = PI;
                System.out.println("ABORT");
            }
            speedFactor = .25 * sin(x) + .25;
        } else if (.5 < currentTime && currentTime <= 1) {
            if (y > PI) {
                y = PI;
                System.out.println("ABORT");
            }
            speedFactor = .25 * sin(y) + .75;
        }
    }
}
