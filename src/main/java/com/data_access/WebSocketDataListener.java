package com.data_access;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.data_storage.DataStorage;
import com.data_storage.PatientRecord;

public class WebSocketDataListener implements DataListener {

    private String serverUri;
    private DataStorage dataStorage;
    private boolean listening;
    private WebSocketClient webSocketClient;

    public WebSocketDataListener(String serverUri, DataStorage dataStorage) {
        this.serverUri = serverUri;
        this.dataStorage = dataStorage;
        this.listening = false;
    }

    @Override
    public void startListening() {
        try {
            webSocketClient = new WebSocketClient(new URI(serverUri)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    listening = true;
                    System.out.println("Connected to server");
                }

                @Override
                public void onMessage(String message) {
                    if (listening) {
                        onDataReceived(message);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    listening = false;
                    System.out.println("Disconnected from server");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopListening() {
        listening = false;
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }

    @Override
    public void onDataReceived(String data) {
        PatientRecord patientData = DataParser.parse(data);
        if (patientData != null) {
            dataStorage.addPatientData(patientData.getPatientId(), patientData.getMeasurementValue(),
                    patientData.getRecordType(), patientData.getTimestamp());
        } else {
            System.out.println("Failed to parse data: " + data);
        }
    }
}
