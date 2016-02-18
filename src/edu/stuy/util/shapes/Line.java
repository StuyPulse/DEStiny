package edu.stuy.util.shapes;

public class Line extends Function{
	public double slope;
	public double yintercept;

	public Line(double slope, double yintercept) {
		this.slope = slope;
		this.yintercept = yintercept;
	}

	public Line(double x1, double y1, double x2, double y2) {
		slope = (y2 - y1) / (x2 - x1);
		yintercept = y1 - slope*x1;
	}

	public double calculate(double input) {
		return yintercept + slope*input;
	}

	public Line getPerpendicular(double x) {
		double returnSlope = -slope;
		double returnIntercept = 2*slope*x + yintercept;
		return new Line(returnSlope, returnIntercept);
	}
	public static double[] getIntercept(Line line1, Line line2) {
		double[] output = new double[2];
		output[0] = (line1.yintercept - line2.yintercept) / (line2.slope - line1.slope);
		output[1] = line1.calculate(output[0]);
		return output;
	}
}