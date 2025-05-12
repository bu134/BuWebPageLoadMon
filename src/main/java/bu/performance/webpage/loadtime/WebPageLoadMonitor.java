package bu.performance.webpage.loadtime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebPageLoadMonitor {
    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        WebDriver driver = new ChromeDriver();
        String csvFilePath = "load_times.csv";
        String chartFilePath = "load_times_chart.png";

        // Initialize recorder and monitor
        LoadTimesRecorder recorder = new LoadTimesRecorder(csvFilePath);
        PerformanceMonitor monitor = new PerformanceMonitor(driver, recorder);
        Thread monitorThread = new Thread(monitor);

        try {
            // Start background monitoring thread
            monitorThread.start();

            // Navigate to web pages to test performance
            driver.get("https://example.com");
            Thread.sleep(3000); // Simulate time for the page to load completely
            driver.get("https://another-example.com");
            Thread.sleep(3000);

            // Stop monitoring once done
            monitor.stopMonitoring();
            monitorThread.join(); // Ensure monitor thread finishes before proceeding

            // Generate graph
            LoadTimeChartGenerator chartGenerator = new LoadTimeChartGenerator();
            chartGenerator.generateChartFromCSV(csvFilePath, chartFilePath);

        } finally {
            driver.quit(); // Clean up WebDriver
        }
    }
}
