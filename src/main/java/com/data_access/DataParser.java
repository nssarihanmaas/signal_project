package com.data_access;

import com.data_storage.PatientRecord;

public class DataParser {

    public static PatientRecord parse(String data) {
        if (data == null) {
            return null;
        }
        String[] parts = data.split(",");
        if (parts.length != 4) {
            return null;
        }

        int patientId = Integer.parseInt(parts[0]);
        double measurementValue = Double.parseDouble(parts[1]);
        String recordType = parts[2];
        long timestamp = Long.parseLong(parts[3]);
        return new PatientRecord(patientId, measurementValue, recordType, timestamp);

    }
}
