package edu.stuy.util;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class TegraSocketReader implements Runnable {

    private AtomicReference<double[]> latestData;

    private ServerSocket socket;
    private Socket tegraClient;

    private static int socketPort = 7054;

    public TegraSocketReader() {
        latestData = new AtomicReference<double[]>();
        latestData.set(null);
        setupSocket();
    }

    private void setupSocket() {
        try {
            System.out.println("About to set up serverSocket");
            socket = new ServerSocket(socketPort);
            System.out.println("Just made serverSocket");
        } catch (IOException e) {
            e.printStackTrace();
            int x = (new int[] {})[111];
        }
    }

    @Override
    public void run() {
        try {
            tegraClient = socket.accept();
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(tegraClient.getInputStream()));

            while (!Thread.currentThread().isInterrupted()) {
                latestData.set(parseMessage(in.readLine()));
                System.out.println(Arrays.toString(latestData.get()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Parse a string of the form "DOUBLE,DOUBLE,DOUBLE\n" into a double[]
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
