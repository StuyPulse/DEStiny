public class TegraThreadManager {
    private TegraDataReader tegraDataReader;
    private Thread tegraThread;

    public TegraThreadManager() {
    }

    /**
     * Start a thread reading vectors from the Tegra, or if this
     * TegraThreadManager has already made such a thread, do nothing.
     *
     * @return false if there already is an alive tegra reader thread
     * running; true otherwise
     */
    public boolean startTegraReadingThread() {
        if (tegraThread != null && tegraThread.isAlive()) {
            return false;
        }
        if (tegraDataReader == null) {
            tegraDataReader = new TegraDataReader();
        }
        tegraThread = new Thread(tegraDataReader);
        tegraThread.setDaemon(true);
        tegraThread.start();
        return true;
    }

    public double[] getMostRecent() {
        return tegraDataReader.getMostRecent();
    }

    /*
     * Interrupt the tegra reading thread, if it exists and is alive.
     *
     * @return false if there was no tegra thread alive; true otherwise
     */
    public boolean interruptThread() {
        if (tegraThread == null || !tegraThread.isAlive()) {
            return false;
        }
        tegraThread.interrupt();
        tegraThread = null;
        return true;
    }
}
