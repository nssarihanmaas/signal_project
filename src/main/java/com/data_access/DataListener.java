package com.data_access;

public interface DataListener {
    void startListening();

    void stopListening();

    void onDataReceived(String data);

}
