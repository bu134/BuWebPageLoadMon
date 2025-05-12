package bu.performance.webpage.loadtime;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadTimeChartGenerator {

    // Generate chart from CSV
    public void generateChartFromCSV(String csvFilePath, String outputFilePath) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean firstLine = true; // Skip header row
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                String url = parts[0]; // Page URL
                double loadTime = Double.parseDouble(parts[1]); // Load time in seconds

                dataset.addValue(loadTime, "Load Time (seconds)", url);
            }
        }

        // Create chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Web Page Performance", // Chart title
                "Page URL",             // Category axis label
                "Load Time (seconds)",  // Value axis label
                dataset
        );

        // Save chart as PNG
        ChartUtils.saveChartAsPNG(new java.io.File(outputFilePath), chart, 800, 600);
        System.out.println("Graph saved as: " + outputFilePath);
    }
}
