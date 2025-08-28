package bu.performance.webpage.loadtime;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

//BU Performance

public class PerformanceMonitor implements Runnable {
    private WebDriver driver;
    private volatile boolean running = true; // Use volatile to ensure visibility across threads
    private LoadTimesRecorder recorder;

    public PerformanceMonitor(WebDriver driver, LoadTimesRecorder recorder) {
        this.driver = driver;
        this.recorder = recorder;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Check if a page is loaded and collect timing
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

                // Get navigation start and DOM complete timing from the browser's performance API
                Long navigationStart = (Long) jsExecutor.executeScript("return window.performance.timing.navigationStart;");
                Long domComplete = (Long) jsExecutor.executeScript("return window.performance.timing.domComplete;");

                if (navigationStart != null && domComplete != null) {
                    double loadTimeInSeconds = (domComplete - navigationStart) / 1000.0; // Convert ms to seconds
                    String currentURL = driver.getCurrentUrl();

                    // Record URL and load time
                    recorder.recordLoadTime(currentURL, loadTimeInSeconds);

                    System.out.println("Recorded: " + currentURL + " - " + loadTimeInSeconds + " seconds");
                }

                Thread.sleep(1000); // Poll every 1 second (adjust polling interval as needed)
            } catch (Exception e) {
                System.err.println("Error in PerformanceMonitor: " + e.getMessage());
            }
        }
    }

    // Stop the monitoring thread
    public void stopMonitoring() {
        this.running = false;
    }
}
