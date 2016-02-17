package edu.stuy.util.shapes;

import edu.stuy.util.Maths;

public class Circle extends Function{
	public double radius;
	public double[] position = new double[2];
	public boolean positive;

	public Circle(double x, double y, double radius) {
		this.position[0] = x;
		this.position[1] = y;
		this.radius = radius;
	}

	public Circle(Line line1, Line line2, Double x1, Double x2, boolean positive) {
		setFromLines(line1, line2, x1, x2);
		this.positive = positive;
	}

	public void setFromLines(Line line1, Line line2, Double x1, Double x2) {
		Line perpendicular1 = line1.getPerpendicular(x1);
		Line perpendicular2 = line2.getPerpendicular(x2);
		position = Line.getIntercept(perpendicular1, perpendicular2);
		radius = Maths.getDistance(x1, line1.calculate(x1), x2, line2.calculate(x2));
	}
	
	public double calculate(double input) {
		// positive: Positive or negative (since circles have 2 outputs)
		if (positive) {
			return position[1] + Math.sqrt(Maths.sqr(radius) - Maths.sqr(input) - Maths.sqr(position[0]));
		}
		return position[1] - Math.sqrt(Maths.sqr(radius) - Maths.sqr(input) - Maths.sqr(position[0]));
	}
}