package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveOverMoatCommand extends DriveForwardCommand {

    private static final int MAX_TIME_IN_SECONDS = 12;
    private static final double SPEED = 0.9;

    public DriveOverMoatCommand() {
        super(SmartDashboard.getNumber("Moat"), MAX_TIME_IN_SECONDS, SPEED);
    }

    @Override
    protected void initialize() {
        super.initialize();
        Robot.dropdown.lowerAcquirerToDrivingPosition();
    }

    @Override
    protected void execute() {
        super.setMaxDistanceInInches(198.0);
        Robot.drivetrain.tankDrive(0.9, 0.9);
    }

    @Override
    protected boolean isFinished() {
        return super.isFinished();
    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    protected void interrupted() {
    }
}
