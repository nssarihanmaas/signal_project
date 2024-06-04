package com.data_storage;

public class PatientData {
    private String patientId;
    private double bloodPressure;
    private double bloodSaturation;
    private double ecg;
    private long timestamp;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public double getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(double bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public double getBloodSaturation() {
        return bloodSaturation;
    }

    public void setBloodSaturation(double bloodSaturation) {
        this.bloodSaturation = bloodSaturation;
    }

    public double getEcg() {
        return ecg;
    }

    public void setEcg(double ecg) {
        this.ecg = ecg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
