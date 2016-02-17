package edu.stuy.robot.commands.auton;

import edu.stuy.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GoOverRoughTerrainCommand extends DriveForwardCommand {

    // private static final int MAX_DISTANCE_IN_INCHES = 132;
    private static final int MAX_TIME_IN_SECONDS = 15;
    private static final double SPEED = 1.0;

    public GoOverRoughTerrainCommand() {
        super(SmartDashboard.getNumber("Rough"), MAX_TIME_IN_SECONDS, SPEED);
    }

    @Override
    protected void initialize() {
        super.initialize();
        Robot.acquirer.lowerAcquirerToDrivingPosition();
    }

    @Override
    protected void execute() {
        super.setMaxDistanceInInches(SmartDashboard.getNumber("Rough"));
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
