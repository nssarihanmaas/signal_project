package com.data_storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient in the data storage system. This class maintains a list of all
 * records associated with the patient, including medical measurements and other data.
 */
public class Patient {
    private int patientId;
    private List<PatientRecord> records;

    /**
     * Constructs a new Patient with a specified patient ID.
     *
     * @param patientId the unique identifier for the patient
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.records = new ArrayList<>();
    }

    /**
     * Adds a record to the patient's list of records.
     *
     * @param record the patient record to add
     */
    public void addRecord(PatientRecord record) {
        records.add(record);
    }

    /**
     * Adds a record to the patient's list of records.
     *
     * @param measurementValue the value of the health metric being recorded
     * @param recordType       the type of record, e.g., "HeartRate", "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in milliseconds since the Unix epoch
     */
    public void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(patientId, measurementValue, recordType, timestamp);
        records.add(record);
    }

    /**
     * Returns the list of all records associated with this patient.
     *
     * @return the list of patient records
     */
    public List<PatientRecord> getAllRecords() {
        return records;
    }

    /**
     * Returns the count of alerts generated for this patient.
     * For simplicity, we consider each record with a high blood pressure value as an alert.
     *
     * @return the count of alerts
     */
    public int getAlertCount() {
        int alertCount = 0;
        for (PatientRecord record : records) {
            if ("BloodPressure".equals(record.getRecordType()) && record.getMeasurementValue() > 140) {
                alertCount++;
            }
        }
        return alertCount;
    }

    public List<PatientRecord> getRecords(long startTime, long endTime) {
        List<PatientRecord> filteredRecords = new ArrayList<>();
        for (PatientRecord record : records) {
            if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }
}
