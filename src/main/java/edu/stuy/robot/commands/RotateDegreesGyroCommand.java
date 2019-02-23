package edu.stuy.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateDegreesGyroCommand extends GyroRotationalCommand {

    private double _angle;
    private boolean notSet;

    public RotateDegreesGyroCommand() {
        super();
        notSet = true;
    }

    public RotateDegreesGyroCommand(double angle) {
        super();
        _angle = angle;
        notSet = false;
    }

    protected void setDesiredAngle() {
        desiredAngle = notSet ? SmartDashboard.getNumber("gyro-rotate-degs", 0) : _angle;
    }

    protected void onEnd() {}
}
