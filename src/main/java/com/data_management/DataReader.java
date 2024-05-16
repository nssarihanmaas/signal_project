package com.data_management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public interface DataReader {
    public static void readData(DataStorage dataStorage, String path) throws IOException {
        List<String[]> records = new ArrayList<>();
        String line;
        String csvSeparator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // Read the header line
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.out.println("Empty CSV file");
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

        for (String[] record : records) {
            if (record.length != 4) {
                throw new IOException("Invalid data format");
            }
            try {
                int patientId = Integer.parseInt(record[0].trim());
                String label = record[2].trim();
                double data = Double.parseDouble(record[3].trim());
                long timestamp = Long.parseLong(record[1].trim());
                dataStorage.addPatientData(patientId, data, label, timestamp);
            } catch (NumberFormatException e) {
                throw new IOException("Invalid data format: Failed to parse numeric values");
            }
        }
    }
}
