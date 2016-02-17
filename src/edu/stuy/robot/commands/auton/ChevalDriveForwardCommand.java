package edu.stuy.robot.commands.auton;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ChevalDriveForwardCommand extends DriveForwardCommand {

    public ChevalDriveForwardCommand(double distance, double time, double speed) {
        super(distance, time, speed);
    }

    @Override
    protected void execute() {
        super.setMaxDistanceInInches(SmartDashboard.getNumber("Cheval"));
        super.execute();
    }

}
