package edu.stuy.robot.subsystems;

import static edu.stuy.robot.RobotMap.*;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Sonar // extends Subsystem
{
	// string is L1234R1234
	// private String data;
	private double[] parser(String s) { // returns in inches
		try {
			String noL = s.substring(1);
			String[] parts = noL.split("R");
			double left = Double.parseDouble(parts[0]);
			double right = Double.parseDouble(parts[1]);
			double[] values = new double[] { left, right };
			return values;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String... args) {
		Sonar s = new Sonar("asdf");
		System.out.println(s.sideToTurn(new double[]{3, 3}));
	}

	/**
	 * isParallel finds the angle of difference between the robot and the wall.
	 * If the error is less then error margin degrees, then it declares the
	 * robot parallel 
	 * <pre>
	 * tan(x) = opposite/adjacent 
	 * adjacent = the distance between the two sensors 
	 * opposite = the difference in distances
	 * 
	 * 
	 *  |\ 
	 *  | \ <- the wall 
	 *  |  \ 
	 *  |   \ 
	 *  |    \ 
	 *d1|------ also the angle 				0 = sensors looking ^ direction 
	 *  |  d2| \ 
	 *  |    |  \ 
	 *  0----0 - - angle we want 
	 *    ^ 
	 *    | 
	 * distance between sonar
	 * </pre>
	 *
	 */
	private boolean isParallel(double[] d) {
		return angleFinder(d) < ERROR_MARGIN_SONAR;
	}

	/*
	 * left = -1 right = 1 parallel = 0
	 */
	private int sideToTurn(double[] d) {
		if (isParallel(d)) {
			return 0;
		} else if (d[0] < d[1]) {
			return -1;
		} else {
			return 1;
		}
	}

	private double angleFinder(double[] d) { // gets input in inches
		double opposite = Math.abs(d[0] - d[1]);
		double adjacent = DISTANCE_BETWEEN_SONAR;
		return Math.toDegrees(Math.atan(opposite / adjacent));
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public Sonar(String s) {
		// data = s;
		// Put the freakin sonar class in here pls
	}
}
