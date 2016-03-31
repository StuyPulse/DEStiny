package edu.stuy.robot.cv.util;

public class FileDoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = -823071855358758802L;

    public FileDoesNotExistException(String filename) {
        super(filename);
    }

}
