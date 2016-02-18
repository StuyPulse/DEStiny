package edu.stuy.util;

public class EfficientRamper extends Ramper{
	private double target;
	private double value;

	private double velocity;
	// For increasing/accelerating.

	private final double STOP_THRESHHOLD = 0.3;
	private final double END_DAMPENING = 0.1;
	private final double START_DAMPENING = 0.001;

	public EfficientRamper() {
		target = 0.0;
		value = 0.0;
		velocity = 0;
	}

	public void update() {
		if (Math.abs(target - value) < STOP_THRESHHOLD) {
			value += (target - value) * END_DAMPENING;
			velocity = 0;
		} else {
			velocity += START_DAMPENING * Math.signum(target - value);
			value += velocity;
		}
	}

	public void setTarget(double target) {
		this.target = target;
	}

	public double getValue() {
		return value;
	}
}