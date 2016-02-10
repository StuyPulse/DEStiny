package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.SerialPort;

public class Sonar extends Subsystem {
	
	SerialPort sonarIn;
	
	public Sonar() {
		sonarIn = new SerialPort(9600, SerialPort.Port.kMXP, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
		sonarIn.enableTermination();
		sonarIn.reset();
	}

	// format of raw data is string "L####R####" (like "L1234R1234")
	private double[] parse(String rawData) { // returns in inches
		try {
			sonarIn.getBytesReceived();
			String dataWithoutL = rawData.substring(1);
			String[] parts = dataWithoutL.split("R");
			double left = Double.parseDouble(parts[0]);
			double right = Double.parseDouble(parts[1]);
			double[] distances = new double[] { left, right };
			return distances;
		} catch (Exception e) {
			return new double[]{0.0, 0.0};
		}
	}

	public double[] getData() {
		String recv = sonarIn.readString();
		return parse(recv);
	}
	
	/**
	 * isParallel finds the angle of difference between the robot and the wall.
	 * If the error is less then error margin degrees, then it declares the
	 * robot parallel
	 * 
	 * <pre>
	 *  tan(x) = opposite/adjacent 
	 *  adjacent = the distance between the two sensors 
	 *  opposite = the difference in distances
	 * 
	 * 
	 *   |\
	 *   | \ <- the wall 
	 *   |  \
	 *   |   \
	 *   |    \
	 * d1|------ also the angle 				0 = sensors looking ^ direction
	 *   |  d2| \
	 *   |    |  \
	 *   0----0 - - angle we want
	 *     ^ 
	 *     | 
	 *  distance between sonar
	 * </pre>
	 *
	 */
	private boolean isParallel(double[] distances) {
		return angleFinder(distances) < ERROR_MARGIN_SONAR;
	}

	/**
	 * <pre>
	 * 	Returns one of three integers:
	 *  left = -1 right = 1 parallel = 0
	 * </pre>
	 */
	private int getSideToTurn(double[] d) {
		if (isParallel(d)) {
			return 0;
		} else if (d[0] < d[1]) {
			return -1;
		} else {
			return 1;
		}
	}

	public double angleFinder(double[] distances) { // gets input in inches
		double opposite = Math.abs(distances[0] - distances[1]);
		double adjacent = DISTANCE_BETWEEN_SONAR;
		return Math.toDegrees(Math.atan(opposite / adjacent));
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}