package edu.stuy.serial;

import java.io.PrintWriter;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import java.util.Arrays;

public class TegraSerialReaderJSSC {
    private SerialPort serialPort;

    public TegraSerialReaderJSSC() {
        String[] ports = SerialPortList.getPortNames();
        if (ports.length > 0) {
            serialPort = new SerialPort(ports[0]);
            System.out.println("Found " + ports.length + " serial ports. Connected to " + ports[0]);
        } else {
            System.out.println("No ports detected at time " + System.currentTimeMillis());
        }
    }

    public double[] readVector() {
        return readVector(null);
    }

    public double[] readVector(PrintWriter writer) {
        // Treat writer like an Option type. It exists for testing.
        boolean verbose = writer != null;

        if (verbose) {
            writer.println("About to read data " + System.currentTimeMillis());
            writer.flush();
        }

        // Read 3 doubles, as there are 3 doubles in each
        // vector we are reading
        double[] vector = new double[3];
        try {
            for (int i = 0; i < 3; i++) {
                byte[] data = serialPort.readBytes(8);
                while (data.length != 8) { // TODO: this not necessary?
                    data = serialPort.readBytes(8);
                }
                vector[i] = Converter.bytesToDouble(data);
            }
        } catch (SerialPortException e) {
            System.out.println("SerialPortException caught. Message: " + e.getMessage());
            return null;
        }
        if (verbose) {
            writer.println("double[3] received: " + Arrays.toString(vector));
            writer.flush();
        }
        if (vector[0] == Double.POSITIVE_INFINITY
                && vector[1] == Double.POSITIVE_INFINITY
                && vector[2] == Double.POSITIVE_INFINITY) {
            // Three +Infinitys is the flag for no goal found
            // There is only one double representation of
            // positive infinity
            return null;
        }
        return vector;
    }

    public void closePort() {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {}
    }
}