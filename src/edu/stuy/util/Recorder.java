package edu.stuy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;

import edu.stuy.robot.Robot;

public class Recorder {

    private LinkedList<LogData> log;

    public Recorder() {
        log = new LinkedList<LogData>();
    }

    public void update() {
        log.add(new LogData(Robot.oi.driverGamepad.getLeftY(), Robot.oi.driverGamepad.getRightY(),
                Robot.oi.operatorGamepad.getRightY(), Robot.drivetrain.gearUp)
        // TODO: The boolean above is the right one, right?
        );
    }

    public void reset() {
        log.clear();
    }
    public int logLength() {
        return log.size();
    }
    public LogData getData(int n) {
        return log.get(n);
    }

    public void writeToFile(String name) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(name));
            for (int i = 0; i < log.size(); i++) {
                writer.write(log.get(i).format());
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("FAILED TO WRITE RECORDED LOG TO FILE");
            // don't write anything!
        }
    }

    public void readFromFile(String name) {
        File file = new File(name);
        FileInputStream stream;
        try {
            stream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            for (String line; ((line = reader.readLine()) != null);) {
                try {
                    LogData data = LogData.fromString(line);
                    log.add(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Don't Add Anything.
                    // TODO: Deal with exception
                }
            }
            reader.close();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            log.add(new LogData(0, 0, 0, false));
            // Just Add Empty-ish log so that the robot actually does something.
        } catch (IOException e) {
            e.printStackTrace();
            // Don't Read Anything!
            System.err.println("PROBLEM READING FILE FROM LOG FILE \"" + name + "\"");
        }
    }
}
