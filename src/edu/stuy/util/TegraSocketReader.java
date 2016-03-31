package edu.stuy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import edu.wpi.first.wpilibj.Timer;
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
    private static final String tegraIP = "10.6.94.19";
    private static final int tegraPort = 7123;

    public TegraSocketReader() {
        latestData = new AtomicReference<double[]>();
        setVal(null);
    }

    private void setVal(double[] val) {
        System.out.println("SETTING TEGRA READ DATA TO: " + (val == null ? "null" : Arrays.toString(val)));
        latestData.set(val);
    }

    /**
     * Creates a socket representing the Tegra, at <code>expectedTegraHost</code>:
     * <code>tegraPort</code>
     */
    private void setupSocket() {
        setupSocketAt(tegraIP);
    }

    /**
     * Creates a socket representing the Tegra, at <code>ip</code>:
     * <code>tegraPort</code>
     * @param ip IP to attempt to connect to
     */
    private void setupSocketAt(String ip) {
        try {
            System.out.println("About to create new Socket at " + ip + ":" + tegraPort);

            connected = false;
            // If the following throws, `connected` remains false

            // Connect to the Tegra
            tegra = new Socket();
            tegra.connect(new InetSocketAddress(ip, tegraPort), 1000);

            if (tegra.isConnected()) {
                connected = true;
                // Set a timeout on Socket IO for when we read data
                tegra.setSoTimeout(1000);
            } else {
                tegra = null;
            }
        } catch (ConnectException e) {
            System.err.println("Failed to connect to " + tegraIP + ":" + tegraPort);
            System.err.println("Exception was: " + e + "\n\n");
        } catch (UnknownHostException e) {
            System.err.println("Could not resolve host at " + tegraIP + ":" + tegraPort);
        } catch (SocketTimeoutException e) {
            System.err.println("Connection to " + ip + ":" + tegraPort + " timed out.");
        } catch (IOException e) {
            System.err.println("An IOException has occurred. Message: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Caught general exception in setupSocketAt: " + e);
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
        while (tegra == null || (tegra != null && !tegra.isConnected())) {
            setupSocket();
        }
        // The following while loop is for trying again and again when
        // connection fails, to make this robust to situations in which
        // the Tegra restarts or has not yet started.
        while (!isInterrupted()) {
            try {
                System.out.println("About to open input stream from Tegra socket");
                // Open input stream and read data until thread is interrupted
                InputStreamReader inStream = new InputStreamReader(tegra.getInputStream());
                BufferedReader bufIn = new BufferedReader(inStream);
                try {
                    while (!isInterrupted()) {
                        // The .readLine() call will timeout after time specified in setupSocketAt
                        double[] msg = parseMessage(bufIn.readLine());
                        setVal(msg);
                        if (SmartDashboard.getBoolean("printTegraData")) {
                            System.out.println("Read data from Tegra: " + Arrays.toString(msg));
                        }
                    }
                } finally {
                    bufIn.close();
                    inStream.close();
                    System.out.println("Closed bufIn and inStream");
                }
            } catch (SocketException e) {
                // Occurs when the server shuts down (or resets)
                System.out.println("Caught: " + e.getMessage() + " Will try to reconnect");
                setupSocket();
                // Create new socket and, via the while loop, try again
            } catch (IOException e) {
                System.out.println("Caught: " + e.getMessage() + " Will try to reconnect");
                setupSocket();
            } catch (Exception e) {
                System.out.println("General exception caught in loop of run(): " + e);
                setupSocket();
            }
        }
        System.out.println("At end of run(): finishing Tegra thread");
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

    private boolean isInterrupted() {
        // Call isInterrupted on currentThread, not Thread.interrupted(),
        // as the latter *resets* the interrupted status and, when called
        // twice in a row, will likely return false: an *unwanted* side-effect
        return Thread.currentThread().isInterrupted();
    }

    // Only run during testing
    public static void main(String[] args) {
        TegraSocketReader tsr = new TegraSocketReader();
        tsr.run();
    }
}
