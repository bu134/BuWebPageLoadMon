package bu.performance.webpage.loadtime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

public class WebPageLoadMonitor {
    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\AbhishekUdayashankar\\Desktop\\BU_local\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        String outputFolderPath = createOutputFolder();
        String csvFilePath = outputFolderPath + "/load_times.csv";
        String chartFilePath = outputFolderPath + "/load_times_chart.png";

        // Initialize recorder and monitor
        LoadTimesRecorder recorder = new LoadTimesRecorder(csvFilePath);
        PerformanceMonitor monitor = new PerformanceMonitor(driver, recorder);
        Thread monitorThread = new Thread(monitor);

        try {
            // Start background monitoring thread
            monitorThread.start();

            // Navigate to web pages to test performance
            driver.get("https://www.cricbuzz.com/");
            Thread.sleep(3000); // Simulate time for the page to load completely
            driver.get("https://www.espncricinfo.com/");
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

    private static String createOutputFolder() {
        String userProvidedName = "Zorawar";

        // Get the current date in YYYY-MM-DD format
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Combine name and date to create folder name
        String folderName = userProvidedName + "_" + currentDate;

        // Create the folder inside the current working directory
        File outputFolder = new File(System.getProperty("user.dir"), folderName);
        if (!outputFolder.exists()) {
            if (outputFolder.mkdirs()) {
                System.out.println("Created output folder: " + outputFolder.getAbsolutePath());
            } else {
                System.err.println("Failed to create output folder. Files will be saved in the current working directory.");
                return System.getProperty("user.dir"); // Fallback: Return current working directory
            }
        }

        return outputFolder.getAbsolutePath();
    }
}
