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
	int FRONT_LEFT_MOTOR_CHANNEL = 2;
	int FRONT_RIGHT_MOTOR_CHANNEL = 1;
	int REAR_LEFT_MOTOR_CHANNEL = 2;
	int REAR_RIGHT_MOTOR_CHANNEL = 2;
	int DRIVER_GAMEPAD = 0;
	int OPERATOR_GAMEPAD = 1;
	int SHOOTER_MOTOR_CHANNEL = 2;
	int FEEDER_MOTOR_CHANNEL = 2;
	int ACQUIRER_MOTOR_CHANNEL = 2;
	int HOOD_SOLENOID_CHANNEL = 2;
	int DROPDOWN_MOTOR_CHANNEL = 2;
	int DRIVETRAIN_GYRO_CHANNEL = 1;
	int WHEEL_DIAMETER = 4;
	int ENCODER_CHANNEL = 68;
	int ACQUIRER_POTENTIOMETER_CHANNEL = 2;
	int EXTRA_HOOD_SOLENOID_CHANNEL = 2;
}
