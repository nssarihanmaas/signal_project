package com.alerts;

import java.util.List;

import com.data_storage.Patient;
import com.data_storage.PatientRecord;

public class BloodPressureStrategy implements AlertStrategy {
    @Override
    public void checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getAllRecords();
        for (PatientRecord patientRecord : records) {
            if ("BloodPressure".equals(patientRecord.getRecordType()) && patientRecord.getMeasurementValue() > 140) {
                AlertFactory factory = new BloodPressureAlertFactory();
                Alert alert = factory.createAlert(patientRecord.getPatientId(), patientRecord.getRecordType(), patientRecord.getTimestamp());
                new AlertGenerator(null).triggerAlert(alert);
            }
        }
    }
}
