package edu.stuy.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Rotate a given number of degrees. The angle to rotate is either
 * set in the constructor parameters, or otherwise read from SmartDashboard. 
 */
public class RotateDegreesGyroCommand extends GyroRotationalCommand {

	/**
	 * Angle (in degrees) through which to rotate. Ignored if {@code useSmartDashboard}
	 * is {@code true}.
	 */
    private double angle;
    private boolean useSmartDashboard;

    public RotateDegreesGyroCommand() {
        super();
        useSmartDashboard = true;
    }

    public RotateDegreesGyroCommand(double angle) {
        super();
        this.angle = angle;
        useSmartDashboard = false;
    }

    @Override
    protected double setDesiredAngle() {
        if (useSmartDashboard) {
        	return SmartDashboard.getNumber("gyro-rotate-degs");
        } else {
        	return angle;
        }
    }

    @Override
    protected void onEnd() {}
}
