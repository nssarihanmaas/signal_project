package com.data_management;

import java.io.IOException;

public class A
{
    public void methodA(){}
}


public interface DataReader {
    

    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    dataType [] readData(DataStorage dataStorage) throws IOException;

    // FileOutputStrategy.java:36
    // onfile: Patient ID: string, Timestamp: string, Label: string, Data: string
    // parsed: Patient ID: int, Timestamp: long, Label: string, Data: double
    // possible labels: Alert, Cholesterol, WhiteBloodCells, RedBloodCells, ECG, Saturation, SystolicPressure, DiastolicPressure
    // Ensure that the data read is accurately passed into the DataStorage for further processing.


    // Unit test: 
    //  - fun read <- mock file, fonksiyona o dosya gönderilir, 4 satır, yükledikten sonra dosyanın boyutu (byte eşitliği), arrayde kaç eleman var? 
    //  - fun parse <- 
}
