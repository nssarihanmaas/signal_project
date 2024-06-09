package com.data_access;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.data_storage.DataStorage;
import com.data_storage.PatientRecord;

public class FileDataListener implements DataListener {

    private String filePath;
    private DataStorage dataStorage;
    private boolean listening;

    public FileDataListener(String filePath, DataStorage dataStorage) {
        this.filePath = filePath;
        this.dataStorage = dataStorage;
    }

    @Override
    public void startListening() {
        listening = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while (listening && (line = reader.readLine()) != null) {
                onDataReceived(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopListening() {
        listening = false;

    }

    @Override
    public void onDataReceived(String data) {
        if (data == null) {
            return;
        }
        PatientRecord patientData = DataParser.parse(data);
        if (patientData != null) {
            dataStorage.addPatientData(patientData.getPatientId(), patientData.getMeasurementValue(),
                    patientData.getRecordType(), patientData.getTimestamp());
        } else {
            System.out.println("Failed to parse data: " + data);
        }
    }

}
