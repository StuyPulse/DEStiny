package edu.stuy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Runnable for setting up a server socket and with it, reading data from a
 * client running on the Tegra
 *
 * @author Berkow
 */
public class TegraSocketReader implements Runnable {

    /**
     * Holds the data of the last message received from the Tegra (a
     * <code>double[3]</code> or <code>null</code>).
     */
    private AtomicReference<double[]> latestData;

    private Socket tegra;
    private boolean connected;

    // tegraHost IP determined by port used on the radio
    private static final String expectedTegraIP = "10.6.94.19";
    private static final int tegraPort = 7123;

    public TegraSocketReader() {
        latestData = new AtomicReference<double[]>();
        latestData.set(null);
        setupSocket();
    }

    /**
     * Creates a socket representing the Tegra, at <code>expectedTegraHost</code>:
     * <code>tegraPort</code>
     */
    private void setupSocket() {
        setupSocketAt(expectedTegraIP);
    }

    /**
     * Creates a socket representing the Tegra, at <code>ip</code>:
     * <code>tegraPort</code>
     * @param ip IP to attempt to connect to
     */
    private void setupSocketAt(String ip) {
        try {
            connected = false;
            tegra = new Socket(ip, tegraPort);
            if (tegra == null) return;
            connected = true;
            SmartDashboard.putString("ip connected to tegra by", ip);
        } catch (ConnectException e) {
            System.out.println("Failed to connect to " + expectedTegraIP + ":" + tegraPort);
        } catch (UnknownHostException e) {
            System.out.println("Could not resolve host at " + expectedTegraIP + ":" + tegraPort);
        } catch (IOException e) {
            System.out.println("An IOException has occurred. Message: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Caught general exception in setupSocketAt: " + e);
        }
    }

    /**
     * Reads lines of data sent from the Tegra Socket. Each line is parsed into
     * an array of three doubles, and <code>latestData</code> is set to this
     * array.
     */
    @Override
    public void run() {
        // Ensure tegra is connected to
        while (tegra == null) {
            setupSocket();
        }
        // The following while loop is for trying again and again when
        // connection fails, to make this robust to situations in which
        // the Tegra restarts or has not yet started.
        while (true) {
            try {
                System.out.println("About to open input stream from Tegra socket");
                // Open input stream and read data until thread is interrupted
                InputStreamReader inStream = new InputStreamReader(tegra.getInputStream());
                BufferedReader bufIn = new BufferedReader(inStream);
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        double[] msg = parseMessage(bufIn.readLine());
                        latestData.set(msg);
                        System.out.println("Read data from Tegra: " + Arrays.toString(msg));
                    }
                } finally {
                    bufIn.close();
                    inStream.close();
                    System.out.println("Closed bufIn and inStream");
                }
                break;
            } catch (SocketException e) {
                // Occurs when the server shuts down (or resets)
                System.out.println("Will try to reconnect");
                setupSocket();
                // Create new socket and, via the while loop, try again
            } catch (IOException e) {
                System.out.println("Will try to reconnect");
                setupSocket();
            } catch (Exception e) {
                System.out.println("General exception caught in loop of run(): " + e);
                setupSocket();
            }
        }
    }

    /**
     * Parses a String of three <code>double</code>s separated by commas into an
     * array of 3 <code>double</code>s
     *
     * @param data
     *     Three <code>double</code> literals separated by commas, with
     *     optional whitespace around each <code>double</code>
     * @return An array of three <code>double</code>s, read from
     *     <code>data</code>
     */
    private static double[] parseMessage(String data) {
        if (data.equals("none")) {
            return null;
        }
        String[] dbls = data.split(",");
        if (dbls.length != 3) {
            return null;
        }
        double[] result = new double[3];
        for (int i = 0; i < dbls.length; i++) {
            // Double.parseDouble will trim the \n or \r\n off the input
            result[i] = Double.parseDouble(dbls[i]);
        }
        return result;
    }

    /**
     * Get the last <code>double[3]</code> received from Tegra
     *
     * @return Last <code>double[3]</code> received from Tegra
     */
    public double[] getMostRecent() {
        return latestData.get();
    }

    /**
     * Returns whether the tegra can be read from.
     * @return
     */
    public boolean isConnected() {
        return connected;
    }

    // Only run during testing
    public static void main(String[] args) {
        TegraSocketReader tsr = new TegraSocketReader();
        tsr.run();
    }
}
