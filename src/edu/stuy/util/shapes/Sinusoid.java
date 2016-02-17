package edu.stuy.util.shapes;

public class Sinusoid extends Function {
	public double target;
	public double initial;
	public Sinusoid(double initial, double target) {
		this.initial = initial;
		this.target = target;
	}

	public double calculate(double input) {
		double amplitude = (target - initial) / 2;
		return amplitude * Math.sin((input - 0.5) * Math.PI) 
				+ initial + amplitude;
	}
}
