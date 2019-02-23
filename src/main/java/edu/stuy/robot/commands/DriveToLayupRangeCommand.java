//package edu.stuy.robot.commands;
//
//import static edu.stuy.robot.RobotMap.LAYUP_SHOOTING_DISTANCE;
//
//import edu.stuy.robot.Robot;
//import edu.stuy.robot.cv.StuyVision;
//import edu.stuy.util.BoolBox;
//
///**
// *
// */
//public class DriveToLayupRangeCommand extends EncoderDrivingCommand {
//
//    public DriveToLayupRangeCommand() {
//        super(Robot.stopAutoMovement);
//        // Use requires() here to declare subsystem dependencies
//        // eg. requires(chassis);
//        requires(Robot.drivetrain);
//    }
//
//    protected void setInchesToMove() {
//        double[] cvReading = Robot.vision.processImage();
//        if (cvReading != null) {
//            double curDistance = StuyVision.findBotDistanceToGoal(cvReading[1]);
//            initialInchesToMove = curDistance - LAYUP_SHOOTING_DISTANCE;
//        } else {
//            // CV failed!
//            cancelCommand = true;
//        }
//        System.out.println(new StuyVision.Report(cvReading));
//        if (cvReading != null) {
//            System.out.println(StuyVision.findBotDistanceToGoal(cvReading[1]));
//        }
//    }
//}
