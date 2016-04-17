package edu.stuy.robot.cv.util;

public class DebugPrinter {

    public static final boolean VERBOSE = false;

    public static void print(Object x) {
        if (VERBOSE) {
            System.out.print(x);
        }
    }

    public static void println(Object x) {
        if (VERBOSE) {
            System.out.println(x);
        }
    }
}
