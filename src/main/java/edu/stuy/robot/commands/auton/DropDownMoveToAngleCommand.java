package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DropDownMoveToAngleCommand extends Command {

    private double desiredAngle;
    private double timeStart; // If potentiometer is disconnected, readings will
    private double initialAngle; // constantly read -91.xx. It will also be
                                 // approx.
                                 // static. If we run the motors for more than
                                 // 2s
                                 // and it still hasn't changed, stop running.

    public DropDownMoveToAngleCommand(double angle) {
        desiredAngle = angle;
        requires(Robot.dropdown);
    }

    @Override
    protected void initialize() {
        timeStart = Timer.getFPGATimestamp();
        initialAngle = Robot.dropdown.getAngle();
    }

    @Override
    protected void execute() {
        if (Robot.dropdown.getAngle() < desiredAngle) {
            Robot.dropdown.move(-1.0);
        } else {
            Robot.dropdown.move(0.75);
        }
    }

    @Override
    protected boolean isFinished() {
        // In case the potentiometer spikes
        if (Robot.dropdown.getAngle() > 1000) {
            System.out.println("POTENTIOMETER SPIKE DURING DROPDOWNMOVETOANGLE");
            return false;
        }
        return Robot.dropdown.getAngle() < -20.0 ||
                !is420Working() ||
                Math.abs(Robot.dropdown.getAngle() - desiredAngle) < 4.0;
    }

    @Override
    protected void end() {
        Robot.dropdown.currentAngle = Robot.dropdown.getAngle();
        Robot.dropdown.move(0.0);
    }

    @Override
    protected void interrupted() {
    }

    private boolean is420Working() {
        if (Timer.getFPGATimestamp() - timeStart < 1.0) {
            return true;
        } else {
            return Math.abs(Robot.dropdown.getAngle() - initialAngle) > 2.0;
        }
    }
}
