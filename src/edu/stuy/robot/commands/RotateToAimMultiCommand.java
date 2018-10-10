//package edu.stuy.robot.commands;
//
//import edu.wpi.first.wpilibj.command.CommandGroup;
//
///**
// *
// */
//public class RotateToAimMultiCommand extends CommandGroup {
//
//    public RotateToAimMultiCommand() {
//        // First rotation:
//        addSequential(new RotateToAimCommand());
//        // Refine:
//        GyroRotationalCommand finalRot = new RotateToAimCommand(true);
//        finalRot.setUseSignalLights(true);
//        addSequential(finalRot);
//    }
//
//    public RotateToAimMultiCommand(double tolerance) {
//        // First rotation:
//        addSequential(new RotateToAimCommand(false, tolerance));
//        // Refine:
//        GyroRotationalCommand finalRot = new RotateToAimCommand(true);
//        finalRot.setUseSignalLights(true);
//        addSequential(finalRot);
//    }
//}
