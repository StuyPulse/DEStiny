/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.commands.DrivetrainFullDriveCommand;

import edu.stuylib.input.Gamepad;
import edu.stuylib.input.gamepads.Logitech;

public class OI {
    public Gamepad driverGamepad;
    public OI(){
        driverGamepad = new Logitech.XMode(0);

        driverGamepad.getRightAnalogButton().whileHeld(new DrivetrainFullDriveCommand());
    }
}
