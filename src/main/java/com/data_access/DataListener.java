package com.data_access;

public interface DataListener {
    void startListenig();

    void stopListening();

    void onDataReceived(String data);

}
