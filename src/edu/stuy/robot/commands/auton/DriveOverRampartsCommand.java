package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveOverRampartsCommand extends DriveForwardCommand {

    private static final int MAX_TIME_IN_SECONDS = 10;
    private static final double SPEED = 1.0;

    public DriveOverRampartsCommand() {
        super(SmartDashboard.getNumber("Ramparts"), MAX_TIME_IN_SECONDS, SPEED);
    }

    @Override
    protected void initialize() {
        super.initialize();
        Robot.dropdown.lowerAcquirerToDrivingPosition();
    }

    @Override
    protected void execute() {
        super.setMaxDistanceInInches(180.0);
        Robot.drivetrain.tankDrive(1.0, 1.0);
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
