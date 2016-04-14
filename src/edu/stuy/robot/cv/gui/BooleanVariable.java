package edu.stuy.robot.cv.gui;

public class BooleanVariable extends Variable {

    public final boolean DEFAULT;
    private boolean val;

    public BooleanVariable(String label, boolean defaultValue) {
        super(label);
        this.DEFAULT = defaultValue;
        this.val = defaultValue;
    }

    public boolean getValue() {
        return val;
    }

    public void setValue(boolean b) {
        val = b;
    }

    public void restoreDefault() {
        val = DEFAULT;
    }

}
