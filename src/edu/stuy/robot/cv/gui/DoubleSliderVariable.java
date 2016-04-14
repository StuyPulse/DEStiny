package edu.stuy.robot.cv.gui;

public class DoubleSliderVariable extends Variable implements NumberVariable {

    public final double DEFAULT, MIN, MAX;
    private double val;

    public DoubleSliderVariable(String label, double defaultVal, double minVal, double maxVal) {
        super(label);
        this.DEFAULT = defaultVal;
        this.MIN = minVal;
        this.MAX = maxVal;
        this.val = defaultVal;
    }

    public double value() {
        return val;
    }

    @Override
    public Number getValue() {
        return value();
    }

    public void set(double d) {
        assert MIN <= d && d <= MAX;
        val = d;
    }

    @Override
    public void setValue(Number n) {
        set(n.doubleValue());
    }

    public void restoreDefault() {
        set(DEFAULT);
    }

}
