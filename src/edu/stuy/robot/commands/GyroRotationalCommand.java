package edu.stuy.robot.commands;

import static edu.stuy.robot.RobotMap.CAMERA_VIEWING_ANGLE_X;
import static edu.stuy.robot.RobotMap.MAX_DEGREES_OFF_AUTO_AIMING;

import edu.stuy.robot.Robot;
import edu.stuy.util.BoolBox;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Abstract command for rotating a certain number of degrees.
 * The angle to rotate is determined at runtime during initialize,
 * by the abstract method <code>getDesiredAngle</code>
 *
 */
public abstract class GyroRotationalCommand extends AutoMovementCommand {

    private enum State {
    	/**
    	 * Indicates command should not end.
    	 */
    	Continue,
    	/**
    	 * Used when setDesiredAngle returns NaN (usually
    	 * intentional). Indicates to cancel the rotation
    	 */
    	QuitNevermind,
    	/**
    	 * Indicates the command should end because there
    	 * was an Exception thrown while running {@code initialize},
    	 * {@code execute}, or {@code isFinished}.
    	 */
    	QuitException;
    	
    	public boolean isQuit() {
    		return this == State.QuitException || this == State.QuitNevermind;
    	}
    }

	/**
	 * Number of degrees to turn. Set dynamically, by {@code setDesiredAngle}.
	 */
    private double desiredAngle;
    
    /**
     * Whether to end execution, and why. See {@code State} enum.
     */
    private State state;

    private boolean priorGearShiftState;

    private boolean gentleRotate;
    private double tolerance;

    private boolean useSignalLights;

    public GyroRotationalCommand() {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        tolerance = MAX_DEGREES_OFF_AUTO_AIMING;
    }

    public GyroRotationalCommand(BoolBox forceStopBox, boolean gentle) {
        super(forceStopBox);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        gentleRotate = gentle;
        tolerance = MAX_DEGREES_OFF_AUTO_AIMING;
    }

    public GyroRotationalCommand(boolean gentle, double tolerance) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        gentleRotate = gentle;
        this.tolerance = tolerance;
    }

    public GyroRotationalCommand(BoolBox forceStopBox, boolean gentle, double tolerance) {
        super(forceStopBox);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        gentleRotate = gentle;
        this.tolerance = tolerance;
    }

    public void setUseSignalLights(boolean use) {
        useSignalLights = use;
    }

    protected abstract double setDesiredAngle();

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        try {
            // If we received a forceStoppedBox controller and it is already
            // true, stop immediately.
            if (externallyStopped()) {
                return;
            }
            super.initialize();
            state = State.Continue;
            priorGearShiftState = Robot.drivetrain.gearUp;
            Robot.drivetrain.resetGyro();

            // Set defaults for values accessible by setDesiredAngle
            desiredAngle = setDesiredAngle();
            if (!Double.isNaN(desiredAngle)) {
            	state = State.QuitNevermind;
            }
        } catch (Exception e) {
            System.out.println("Error in intialize in RotateToAimCommand:");
            e.printStackTrace();
            state = State.QuitException;
        }
    }

    // INCREASE these if it is OVERshooting
    // DECREASE these if it is UNDERshooting
    private double TUNE_FACTOR = 1;
    private double TUNE_OFFSET = 0.0;
    private double angleMoved() {
        double gyro = Robot.drivetrain.getGyroAngle();
        if (gyro > 180) {
            return gyro - 360;
        }
        return gyro * TUNE_FACTOR + TUNE_OFFSET;
    }

    private double degreesToMove() {
        return desiredAngle - angleMoved();
    }

    private double howMuchWeHaveToGo() {
        // Used for ramping
        return Math.abs(degreesToMove() / (CAMERA_VIEWING_ANGLE_X / 2));
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        try {
            super.execute();
            if (!getForceStopped()) {
                double speed = gentleRotate
                        ? 0.53 + 0.15 * Math.pow(howMuchWeHaveToGo(), 2)
                        : 0.60 + 0.30 * Math.pow(howMuchWeHaveToGo(), 2);
                System.out.println("\n\n\n\n\n\n\nSpeed to use:\t" + speed);
                System.out.println("getGyroAngle():\t" + Robot.drivetrain.getGyroAngle());
                System.out.println("angleMoved():\t" + angleMoved());
                System.out.println("desiredAngle:\t" + desiredAngle);
                System.out.println("degreesToMove():\t" + degreesToMove());
                // right is negative when turning right
                if (degreesToMove() < 0) {
                    System.out.println("\nMoving left, as degreesToMove()=" + desiredAngle + " < 0");
                    System.out.println("So: tankDrive(" + -speed + ", " + speed + ")\n");
                    Robot.drivetrain.tankDrive(-speed, speed);
                } else {
                    System.out.println("\nMoving RIGHT, as degreesToMove()=" + desiredAngle + " > 0");
                    System.out.println("So: tankDrive(" + speed + ", " + -speed + ")\n");
                    Robot.drivetrain.tankDrive(speed, -speed);
                }
            }
        } catch (Exception e) {
            System.out.println("\n\n\n\n\nError in execute in RotateToAimCommand:");
            e.printStackTrace();
            state = State.QuitException;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        try {
            if (getForceStopped()) {
                return true;
            }
            // When no more can or should be done:
            if (state.isQuit() || Math.abs(desiredAngle) < 0.001) {
                // The last condition above is *not* the judgment of whether aiming has
                // succeeded; it is a failsafe for cases in which desiredAngle is 0
                Robot.cvSignalLight.stayOff();
                System.out.println("\n\n\n\n\n\n\nstate: " + state + "\ndesiredAngle: " + desiredAngle);
                return true;
            }

            // Judgment of success:
            double degsOff = degreesToMove();
            SmartDashboard.putNumber("CV degrees off", degsOff);

            boolean onTarget = Math.abs(degsOff) < tolerance;
            System.out.println("degsOff: " + degsOff + "\nonTarget: " + onTarget);

            // Set CV LED lights to indicate to drivers whether we are aligned yet.
            if (useSignalLights) {
                if (onTarget) {
                    Robot.cvSignalLight.stayOn();
                } else {
                    Robot.cvSignalLight.stayOff();
                }
                Robot.blueSignalLight.setBlinking(onTarget);
            }

            return onTarget;
        } catch (Exception e) {
            System.out.println("Error in isFinished in RotateToAimCommand:");
            e.printStackTrace();
            state = State.QuitException;
            return true; // abort right now
        }
    }

    abstract protected void onEnd();

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.stop();
        System.out.println("ENDED");

        // TODO: The following, as this begins always in low gear and
        // does not change the gear, should do nothing. Test having
        // removed it to be absolutely sure.
        Robot.drivetrain.manualGearShift(priorGearShiftState);

        onEnd();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.drivetrain.tankDrive(0.0, 0.0);
    }
}