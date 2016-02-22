package edu.stuy.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import edu.wpi.first.wpilibj.SerialPort;

public class TegraDataReader implements Runnable {

    private SerialPort serialPort;
    private AtomicReference<double[]> mostRecentValue;

    public TegraDataReader() {
        SerialPort.Port p = SerialPort.Port.kOnboard;
        serialPort = new SerialPort(9600, p, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
        mostRecentValue = new AtomicReference<double[]>();
    }

    @Override
    public void run() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("/tmp/tegra-data-output.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        readVectorsConstantly(writer);
    }

    public double[] getMostRecent() {
        return mostRecentValue.get();
    }

    private void readVectorsConstantly() {
        readVectorsConstantly(null);
    }

    private void readVectorsConstantly(PrintWriter writer) {
        // Treat writer like an Option type, so that a writer
        // may be easily passed for testing.
        boolean verbose = writer != null;
        for (;;) {
            // Read 3 doubles, as there are 3 doubles in each
            // vector we are reading
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
                writer.println("double[3] received: " + Arrays.toString(vector));
                writer.flush();
            }
            double[] valToSave = vector;
            if (vector[0] == Double.POSITIVE_INFINITY && vector[1] == Double.POSITIVE_INFINITY
                    && vector[2] == Double.POSITIVE_INFINITY) {
                // Three +Infinitys is the flag for no goal found
                // There is only one double representation of
                // positive infinity
                valToSave = null;
            }
            mostRecentValue.set(valToSave);
        }
    }
}
