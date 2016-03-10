package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;

public class DriveOverRockWallCommand extends DriveForwardCommand {

    private static final int MAX_TIME_IN_SECONDS = 15;
    private static final double SPEED = 1.0;

    public DriveOverRockWallCommand() {
        super(12.0, MAX_TIME_IN_SECONDS, SPEED);
    }

    @Override
    protected void initialize() {
        super.initialize();
        Robot.dropdown.lowerAcquirerToDrivingPosition();
    }

    @Override
    protected void execute() {
        super.setMaxDistanceInInches(168.0);
        super.execute();
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
