package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveOverRoughTerrainCommand extends DriveForwardCommand {

    private static final int MAX_DISTANCE_IN_INCHES = 144;
    private static final int MAX_TIME_IN_SECONDS = 15;
    private static final double SPEED = 0.7;

    public DriveOverRoughTerrainCommand() {
        super(SmartDashboard.getNumber("Rough"), MAX_TIME_IN_SECONDS, SPEED);
    }

    @Override
    protected void initialize() {
        super.initialize();
        Robot.dropdown.lowerAcquirerToDrivingPosition();
    }

    @Override
    protected void execute() {
        super.setMaxDistanceInInches(SmartDashboard.getNumber("Rough"));
        Robot.drivetrain.tankDrive(0.7, 0.7);
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
