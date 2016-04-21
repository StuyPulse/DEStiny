package edu.stuy.robot.cv.gui;

public abstract class Variable {

    public final String LABEL;

    public Variable(String label) {
        this.LABEL = label;
    }

    public abstract void restoreDefault();

}
