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
            Robot.dropdown.move(-0.4);
        } else {
            Robot.dropdown.move(0.55);
        }
    }

    @Override
    protected boolean isFinished() {
        return Robot.dropdown.getAngle() < -20.0 ||
                !isPotentiometerWorking() ||
                Math.abs(Robot.dropdown.getAngle() - desiredAngle) < 4.0;
    }

    @Override
    protected void end() {
        Robot.dropdown.currentAngle = desiredAngle;
        Robot.dropdown.move(0.0);
    }

    @Override
    protected void interrupted() {
    }

    private boolean isPotentiometerWorking() {
        if (Timer.getFPGATimestamp() - timeStart < 1.0) {
            return true;
        } else {
            return Math.abs(Robot.dropdown.getAngle() - initialAngle) > 2.0;
        }
    }
}
