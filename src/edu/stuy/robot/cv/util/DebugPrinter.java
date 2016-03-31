package edu.stuy.robot.cv.util;

public class DebugPrinter {

    public static final boolean VERBOSE = false;

    public static void println(Object x) {
        if (VERBOSE) {
            System.out.println(x);
        }
    }
}
