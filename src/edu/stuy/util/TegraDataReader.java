package edu.stuy.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import edu.wpi.first.wpilibj.SerialPort;

public class TegraDataReader implements Runnable {
    private SerialPort serialPort;

    public TegraDataReader() {
        SerialPort.Port p = SerialPort.Port.kOnboard;
        serialPort = new SerialPort(9600, p, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
    }

    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter("/tmp/tegra-data-output.txt", "UTF-8");
            readVectorsConstantly(writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void readVectorsConstantly(PrintWriter writer) {
        boolean verbose = writer != null;
        for (;;) {
            // Read 3 doubles, as there are 3 doubles in each
            // vector we are reading
            if (verbose) {
                writer.println("About to read a byte people " + System.currentTimeMillis());
                writer.flush();
            }
            double[] vector = new double[3];
            for (int i = 0; i < 3; i++) {
                byte[] data = serialPort.read(8);
                // TODO: Test with serialPort.setTimeout rather than
                // this loop
                while (data.length != 8) {
                    data = serialPort.read(8);
                }
                vector[i] = Converter.bytesToDouble(data);
            }
            if (verbose) {
                writer.println("Double whose bytes are to be printed is: " + Arrays.toString(vector));
                writer.flush();
            }
        }
    }
}
