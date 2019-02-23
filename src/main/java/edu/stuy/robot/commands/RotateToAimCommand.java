//package edu.stuy.robot.commands;
//
//import java.util.Arrays;
//
//import edu.stuy.robot.Robot;
//import edu.stuy.robot.cv.StuyVision;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//
///**
// *
// */
//public class RotateToAimCommand extends GyroRotationalCommand {
//
//    public RotateToAimCommand() {
//        // Use requires() here to declare subsystem dependencies
//        // eg. requires(chassis);
//        super(Robot.stopAutoMovement, false);
//    }
//
//    public RotateToAimCommand(boolean gentle) {
//        // Use requires() here to declare subsystem dependencies
//        // eg. requires(chassis);
//        super(Robot.stopAutoMovement, gentle);
//    }
//
//    public RotateToAimCommand(boolean gentle, double tolerance) {
//        // Use requires() here to declare subsystem dependencies
//        // eg. requires(chassis);
//        super(Robot.stopAutoMovement, gentle, tolerance);
//    }
//
//    private double[] cvReading;
//
//    protected void setDesiredAngle() {
//        cvReading = Robot.vision.processImage();
//        canProceed = cvReading != null;
//        SmartDashboard.putString("cv-reading", Arrays.toString(cvReading));
//        if (canProceed) {
//            desiredAngle = StuyVision.frameXPxToDegrees(cvReading[0]);
//            SmartDashboard.putNumber("cv-angle", desiredAngle);
//        }
//        SmartDashboard.putBoolean("cv-visible", canProceed);
//        // For auton:
//        Robot.cvFoundGoal = canProceed;
//    }
//
//    protected void onEnd() {
//        System.out.println(new StuyVision.Report(cvReading));
//    }
//}
