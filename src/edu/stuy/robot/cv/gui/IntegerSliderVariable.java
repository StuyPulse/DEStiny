package edu.stuy.robot.cv.gui;

public class IntegerSliderVariable extends Variable implements NumberVariable {

    public final int DEFAULT, MIN, MAX;
    private int val;

    public IntegerSliderVariable(String label, int defaultVal, int minVal, int maxVal) {
        super(label);
        this.DEFAULT = defaultVal;
        this.MIN = minVal;
        this.MAX = maxVal;
        this.val = defaultVal;
    }

    public int value() {
        return val;
    }

    @Override
    public Number getValue() {
        return value();
    }

    public void set(int n) {
        assert MIN <= n && n <= MAX;
        val = n;
    }

    @Override
    public void setValue(Number n) {
        set(n.intValue());
    }

    public void restoreDefault() {
        set(DEFAULT);
    }

}
