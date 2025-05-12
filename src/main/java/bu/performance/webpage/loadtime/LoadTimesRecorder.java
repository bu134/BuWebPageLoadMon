package bu.performance.webpage.loadtime;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantLock;

public class LoadTimesRecorder {
    private final String filePath;
    private final ReentrantLock lock = new ReentrantLock(); // Ensure thread safety

    public LoadTimesRecorder(String filePath) {
        this.filePath = filePath;

        // Initialize the CSV with a header row if it doesn’t exist
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println("Page URL,Load Time (seconds)");
        } catch (IOException e) {
            System.err.println("Error initializing CSV file: " + e.getMessage());
        }
    }

    // Record load time into the CSV file
    public void recordLoadTime(String url, double loadTimeInSeconds) {
        lock.lock(); // Ensure only one thread writes at any time
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.printf("%s,%.2f%n", url, loadTimeInSeconds); // Format to 2 decimal places
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
