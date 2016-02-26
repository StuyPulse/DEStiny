package edu.stuy.util;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Runnable for setting up a server socket and with it, reading data
 * from a client running on the Tegra
 * @author Berkow
 *
 */
public class TegraSocketReader implements Runnable {

    /**
     * Holds the array of three <code>double</code>s most recently read from the
     * Tegra. If nothing has yet been read, or if the failure value (three
     * <code>Infinity</code>s) is received, it contains <code>null</code>.
     */
    private AtomicReference<double[]> latestData;

    private ServerSocket socket;
    private Socket tegraClient;

    private static final int socketPort = 7054;

    public TegraSocketReader() {
        latestData = new AtomicReference<double[]>();
        latestData.set(null);
        setupSocket();
    }

    /**
     * Creates a server socket on port 7054
     */
    private void setupSocket() {
        try {
            socket = new ServerSocket(socketPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads lines of data sent to the ServerSocket. Each line
     * is parsed into an array of three doubles, and <code>latestData</code>
     * is set to this array.
     */
    @Override
    public void run() {
        try {
            tegraClient = socket.accept();
            System.out.println("Accepting data from tegra socket");
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(tegraClient.getInputStream()));

            while (!Thread.currentThread().isInterrupted()) {
                latestData.set(parseMessage(in.readLine()));
                System.out.println(Arrays.toString(latestData.get()));
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses three doubles separated by commas into an array of 3
     * <code>double</code>s
     * @param data Three <code>double</code> literals separated by commas,
     * with optional whitespace around each <code>double</code>
     * @return An array of three <code>double</code>s, read from
     * <code>data</code>
     */
    private static double[] parseMessage(String data) {
        String[] dbls = data.split(",");
        if (dbls.length < 3) {
            return null;
        }
        double[] result = new double[3];
        for (int i = 0; i < dbls.length; i++) {
            // trim() takes off trailing \n
            result[i] = Double.parseDouble(dbls[i].trim());
        }
        if (result[0] == Double.POSITIVE_INFINITY
                && result[1] == Double.POSITIVE_INFINITY
                && result[2] == Double.POSITIVE_INFINITY) {
            return null;
        }
        return result;
    }
    
    public double[] getMostRecent() {
        return latestData.get();
    }
}
