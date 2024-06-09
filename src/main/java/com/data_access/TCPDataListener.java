package com.data_access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.data_storage.DataStorage;
import com.data_storage.PatientRecord;

public class TCPDataListener implements DataListener {

    private String serverAddress;
    private int port;
    private DataStorage dataStorage;
    private boolean listening;
    private Socket socket;

    public TCPDataListener(String serverAddress, int port, DataStorage dataStorage) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.dataStorage = dataStorage;
        this.listening = false;
    }

    public TCPDataListener(Socket socket, DataStorage dataStorage) {
        this.socket = socket;
        this.dataStorage = dataStorage;
        this.listening = false;
    }

    @Override
    public void startListening() {
        listening = true;
        try {
            if (socket == null) {
                socket = new Socket(this.serverAddress, this.port);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                System.out.println("Connected to the server at " + serverAddress + ":" + port);
                String line;
                while (listening && (line = reader.readLine()) != null) {
                    onDataReceived(line);
                }

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
