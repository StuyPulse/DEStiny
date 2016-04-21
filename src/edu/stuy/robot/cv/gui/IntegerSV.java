package edu.stuy.robot.cv.gui;

public class IntegerSV extends Variable implements NumberVariable {

    public final int DEFAULT, MIN, MAX;
    private int val;

    public IntegerSV(int defaultVal, int minVal, int maxVal, String label) {
        super(label);
        this.DEFAULT = defaultVal;
        this.MIN = minVal;
        this.MAX = maxVal;
        this.val = defaultVal;
    }

    public static IntegerSV mkColor(int defaultVal, String label) {
        return new IntegerSV(defaultVal, 0, 255, label);
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
