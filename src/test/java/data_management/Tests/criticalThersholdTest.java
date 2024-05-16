package data_management.Tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class criticalThersholdTest {
    String csvFilePath_1="/Usersnehir/OneDrive/Belgeler/GitHub/signal_project/src/test/java/data_management/Tests/critical_threshold_test.csv";
        List<String[]> records = readCSV(csvFilePath_1);
        //checkAlerts(records);
    }

    public static List<String[]> readCSV(String csvFile) {
        List<String[]> records = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Read the header line
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.out.println("Empty CSV file");
                return records;
            }

            // Process each remaining line
            while ((line = br.readLine()) != null) {
                // Split the line into tokens
                String[] values = line.split(csvSeparator);
                records.add(values);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }
