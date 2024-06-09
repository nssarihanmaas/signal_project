package com.alerts;

public class BloodPressureAlert extends Alert {
    public BloodPressureAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
