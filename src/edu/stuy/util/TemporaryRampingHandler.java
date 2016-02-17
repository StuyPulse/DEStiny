package edu.stuy.util;

public class TemporaryRampingHandler {
	private double initial;
	private double target;// Would be called "final", but final is reserved

	private double counter;
	// Input of function (increases linearly)

	public double rampSpeed;
	// Increase of function (its speed)

	public boolean finished;
	// For now it's a utility. Could be removed.

	public boolean allowOverride = true;
	// If false, function waits till completion until changing value

	public boolean interpolate = true;
	// If true, value smoothly "snaps" onto function.
	public double interpolationSpeed = 0.2;
	// How quickly function "snaps" into place.
	// If 1, it's instant. If 0, it never moves.
	public double interpolationValue = 0;
	// Stores previous figured value.

	public TemporaryRampingHandler(double initial, double target, double rampSpeed) {
		this.initial = initial;
		this.target = target;
		counter = 0;
		this.rampSpeed = rampSpeed;
		interpolationValue = initial;
	}

	private double equation(double input) {
		/* INPUT:
		 * BETWEEN 0 AND 1
		 * 0: Start of function (x = initial)
		 * 1: End of function   (x = target)
		 */
		return linearEquation(input, target, initial);
	}

	private double linearEquation(double input, double target, double initial) {
		return (target - initial) * input + initial;
	}

	private double sineEquation(double input, double target, double initial) {
		double amplitude = (target - initial) / 2;
		return amplitude * Math.sin((input- 0.5) * Math.PI) 
				+ initial + amplitude;
	}

	public void update() {
		counter += rampSpeed;
		if (counter > 1) {
			counter = 1;
			finished = true;
		} else {
			finished = false;
		}
	}

	public double getValue() {
		if (interpolate) {
			interpolationValue = interpolateLinear(interpolationValue);
			return interpolationValue;
		}
		return equation(counter);
	}

	public void setTarget(double target) {
		// Sets to new target and resets equation.
		// Might need to switch this such that it dynamically updates...
		if (allowOverride || finished) {
			this.target = target;
			this.counter = 0;
			if (!interpolate) {
				/*
				 * This check is here because if there is interpolation,
				 * we don't want our function to continue from our previous
				 * one.
				 */
				this.initial = getValue();
			}
		}
	}

	private double interpolateSine(double value) {
		return -1;
		// WIP
		// THIS IS THE REAL DEAL. ONCE WE HAVE THIS DONE, WE SHOULD BE AYE OKAY
		//value = value +
		//sineEquation((equation(value) - value));
	}

	private double interpolateMultiply(double value) {
		return value + 
				(equation(counter) - value) * interpolationSpeed;
	}

	private double interpolateLinear(double value) {
		double result = value + interpolationSpeed;
		if (result > equation(counter)) {
			result = equation(counter);
		}
		return result;
	}
	private void tickLoop() {
		Thread thread = new Thread();
	}
}