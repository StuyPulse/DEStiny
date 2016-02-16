package edu.stuy.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public interface RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	int FRONT_LEFT_MOTOR_CHANNEL = 4;
	int FRONT_RIGHT_MOTOR_CHANNEL = 1;
	int REAR_LEFT_MOTOR_CHANNEL = 3;
	int REAR_RIGHT_MOTOR_CHANNEL = 2;

	int HOPPER_MOTOR_CHANNEL = 5;
	int ACQUIRER_MOTOR_CHANNEL = 6;
	int SHOOTER_MOTOR_CHANNEL = 7;
	int DROPDOWN_MOTOR_CHANNEL = 8;

	int GEAR_SHIFT_CHANNEL = 9;
	int GEAR_SHIFT_THRESHOLD = 0;

	int DRIVER_GAMEPAD = 0;
	int OPERATOR_GAMEPAD = 1;

	int LIMIT_SWITCH_CHANNEL = 0;

	int HOOD_SOLENOID_CHANNEL = 0;
	int SHOOTER_WHEEL_DIAMETER = 4;

	int SHOOTER_ENCODER_A_CHANNEL = 0;
	int SHOOTER_ENCODER_B_CHANNEL = 1;
	int ACQUIRER_POTENTIOMETER_CHANNEL = 0;

	double PID_MAX_ROBOT_SPEED = 0.75;
	double GYRO_P = 1.0;
	double GYRO_I = 1.0;
	double GYRO_D = 1.0;

	int PULSES_PER_REVOLUTION = 360;
	int ERROR_MARGIN_SONAR = 5;
	int DRIVETRAIN_WHEEL_DIAMETER = 8;
	double DRIVETRAIN_WHEEL_CIRCUMFERENCE = DRIVETRAIN_WHEEL_DIAMETER * Math.PI;
	double DRIVETRAIN_ENCODER_INCHES_PER_PULSE = DRIVETRAIN_WHEEL_CIRCUMFERENCE
			/ PULSES_PER_REVOLUTION;
	double DISTANCE_BETWEEN_SONAR = 1.0;

	double WHEEL_DIAMETER_DRIVETRAIN = 4;

	int LEFT_ENCODER_CHANNEL_A = 0;
	int LEFT_ENCODER_CHANNEL_B = 1;
	int RIGHT_ENCODER_CHANNEL_A = 2;
	int RIGHT_ENCODER_CHANNEL_B = 3;

	String SHOOTER_SPEED_LABEL = "Shooter Speed";
	// Auton
	double ROCK_WALL_CURRENT_THRESHOLD = 0.0;

	// CV
	double MAX_DEGREES_OFF_AUTO_AIMING = 5;
	int CAMERA_FRAME_PX_WIDTH = 1280;
	int CAMERA_FRAME_PX_HEIGHT = 720;
	int CAMERA_VIEWING_ANGLE_X = 180; // This is most likely wrong
}
