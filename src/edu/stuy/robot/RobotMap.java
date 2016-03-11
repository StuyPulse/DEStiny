package edu.stuy.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public interface RobotMap {

    // Gamepad ports
    int DRIVER_GAMEPAD = 0;
    int OPERATOR_GAMEPAD = 1;

    // Drivetrain CANTalon channels
    int FRONT_RIGHT_MOTOR_CHANNEL = 1;
    int REAR_RIGHT_MOTOR_CHANNEL = 2;
    int REAR_LEFT_MOTOR_CHANNEL = 3;
    int FRONT_LEFT_MOTOR_CHANNEL = 4;

    // CANTalon channels for other subsystems
    int HOPPER_MOTOR_CHANNEL = 5;
    int ACQUIRER_MOTOR_CHANNEL = 6;
    int SHOOTER_MOTOR_CHANNEL = 7;
    int DROPDOWN_MOTOR_CHANNEL = 8;

    // Solenoid ports
    int HOOD_SOLENOID_CHANNEL = 6;
    int GEAR_SHIFT_CHANNEL = 3;

    // Tuneable threshold for current (used in gear shifting for drivetrain)
    int GEAR_SHIFT_THRESHOLD = 40;

    // DropDown Constants
    double DROP_DOWN_DEADBAND = 0.01;

    // Hood Constants
    boolean HOOD_UP_POSITION = false;
    boolean HOOD_DOWN_POSITION = true;

    // Hopper Constants
    double HOPPER_SENSOR_THRESHOLD = 1.0;

    // Digital IO Ports
    int LEFT_ENCODER_CHANNEL_A = 0;
    int LEFT_ENCODER_CHANNEL_B = 1;
    int RIGHT_ENCODER_CHANNEL_A = 2;
    int RIGHT_ENCODER_CHANNEL_B = 3;
    int SHOOTER_ENCODER_A_CHANNEL = 4;
    int SHOOTER_ENCODER_B_CHANNEL = 5;
    int LIMIT_SWITCH_CHANNEL = 6;
    int FLASHLIGHT_CHANNEL = 7;

    // Analog ports
    int ACQUIRER_POTENTIOMETER_CHANNEL = 0;
    int HOPPER_SENSOR_CHANNEL = 3;

    // Potentiometer
    double INITIAL_VOLTAGE = 287.5; // Equal to 0 degrees
    double CONVERSION_FACTOR = 11.25;
    double EPSILON = 0.0005;

    // Physical constants
    int SHOOTER_WHEEL_DIAMETER = 4;
    double SHOOTER_ENCODER_MAXSPEED = 3600.0;
    int DIO_ENCODER_PULSES_PER_REVOLUTION = 360;
    int DRIVETRAIN_WHEEL_DIAMETER = 8;
    double DRIVETRAIN_WHEEL_CIRCUMFERENCE = DRIVETRAIN_WHEEL_DIAMETER * Math.PI;
    double DRIVETRAIN_ENCODER_INCHES_PER_PULSE = DRIVETRAIN_WHEEL_CIRCUMFERENCE / DIO_ENCODER_PULSES_PER_REVOLUTION;
    double DISTANCE_BETWEEN_SONAR = 1.0;

    // Sonar
    int SONAR_ERROR_MARGIN = 5;
    double DISTANCE_TO_WALL = 14 * 12; // 14ft in inches

    // PID tuning values
    double PID_MAX_ROBOT_SPEED = 0.75;
    double GYRO_P = 1.0;
    double GYRO_I = 1.0;
    double GYRO_D = 1.0;

    // Smart Dashboard
    String SHOOTER_SPEED_LABEL = "Shooter Speed";

    // Auton
    double ROCK_WALL_CURRENT_THRESHOLD = 0.0;
    int ARM_CROSSING_OBSTACLE_ANGLE = 50;

    double SLOT_ANGLE_TO_GOAL_1 = 39.614; // UNUSED (lowbar)
    double SLOT_ANGLE_TO_GOAL_2 = 29.535;
    double SLOT_ANGLE_TO_GOAL_3 = 16.987;
    double SLOT_ANGLE_TO_GOAL_4 = -12.228;
    double SLOT_ANGLE_TO_GOAL_5 = -25.539;

    // CV
    double MAX_DEGREES_OFF_AUTO_AIMING = 2;
    int CAMERA_FRAME_PX_WIDTH = 1280;
    int CAMERA_FRAME_PX_HEIGHT = 720;
    int CAMERA_VIEWING_ANGLE_X = 60;
    double IDEAL_VERTICAL_OFFSET_AUTO_AIMING = 70;
    double MAX_VERTICAL_PX_OFF_AUTO_AIMING = 20;

    // Signal Lights
    int SIGNAL_LIGHT_RED_PORT = 4;
    int SIGNAL_LIGHT_YELLOW_PORT = 5;
    int SIGNAL_LIGHT_BLUE_PORT = 6;

    // Operator IDs
    int JONAH_ID = 0;
    int YUBIN_ID = 1;
}
