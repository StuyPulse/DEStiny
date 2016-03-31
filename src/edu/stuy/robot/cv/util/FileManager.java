package edu.stuy.robot.cv.util;

import java.io.File;

public class FileManager {

    public static boolean fileExists(String filename) {
        try {
            return new File(filename).exists();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void assertFileExists(String filename) {
        if (!fileExists(filename)) {
            throw new FileDoesNotExistException(filename);
        }
    }

}
