package edu.stuy.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateDegreesGyroCommand extends GyroRotationalCommand {

    private double _angle;

    public RotateDegreesGyroCommand() {
        super();
        _angle = SmartDashboard.getNumber("gyro-rotate-degs");
    }

    public RotateDegreesGyroCommand(double angle) {
        super();
        _angle = angle;
    }

    protected void setDesiredAngle() {
        desiredAngle = _angle;
    }
}
