package com.data_management;

import java.io.IOException;

class Row {
    int patientId;
    long timestamp;
    String label;
    String data;

    public static Row create(int patientId, long timestamp, String label, String data) {
        Row rowFirst = new Row();
        rowFirst.patientId = patientId;
        rowFirst.timestamp = timestamp;
        rowFirst.label = label;
        rowFirst.data = data;
        return rowFirst;
    }
}


public interface DataReader {

    @Override
    public String[] readData(DataStorage dataStorage, String path) throws IOException {
        // Read all lines from the file
        List<String> lines = Files.readAllLines(Paths.get(path));
        // Parse the data
        Row[] rows = parseData(lines.toArray(new String[0]));
        // Store the parsed data in the DataStorage
        for (Row row : rows) {
            dataStorage.storeData(row);
        }
        // Return the loaded data as an array of strings
        return lines.toArray(new String[0]);
    }

    @Override
    public Row[] parseData(String[] loadedData) throws IOException {
        List<Row> rows = new ArrayList<>();
        for (String line : loadedData) {
            String[] parts = line.split(",");
            if (parts.length != 4) {
                throw new IOException("Invalid data format");
            }
            try {
                int patientId = Integer.parseInt(parts[0].trim());
                long timestamp = Long.parseLong(parts[1].trim());
                String label = parts[2].trim();
                String data = parts[3].trim();
                Row row = Row.create(patientId, timestamp, label, data);
                rows.add(row);
            } catch (NumberFormatException e) {
                throw new IOException("Invalid data format: Failed to parse numeric values");
            }
        }
        return rows.toArray(new Row[0]);
    }

}
