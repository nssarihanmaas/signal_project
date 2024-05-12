package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;


/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
    
   // List<PatientRecord> records = patient.getRecords(1700000000000L, 1800000000000L);
    
    //trend alert
    static class checkTrendAlert(List<PatientRecord> records) {
        if (records.size() < 3) {
            // Not enough records to check trend alert
            return;
        }
        
        for (int i = 0; i < records.size() - 2; i++) {
            PatientRecord currentRecord = records.get(i);
            PatientRecord nextRecord = records.get(i + 1);
            PatientRecord nextNextRecord = records.get(i + 2);
            
            // Check if the systolic and diastolic blood pressure readings change by more than 10 mmHg
            if (Math.abs(nextRecord.getSystolicBloodPressure() - currentRecord.getSystolicBloodPressure()) > 10 &&
                Math.abs(nextNextRecord.getSystolicBloodPressure() - nextRecord.getSystolicBloodPressure()) > 10 &&
                Math.abs(nextRecord.getDiastolicBloodPressure() - currentRecord.getDiastolicBloodPressure()) > 10 &&
                Math.abs(nextNextRecord.getDiastolicBloodPressure() - nextRecord.getDiastolicBloodPressure()) > 10) {
                // Trigger trend alert
                triggerAlert(new Alert("Trend Alert", "Consistent increase or decrease in blood pressure detected."));
            }
        }
    }
    
    
    
    
    //critical threshold alert
    static class checkCriticalThresholdAlert(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            double systolicBP = record.getSystolicBloodPressure();
            double diastolicBP = record.getDiastolicBloodPressure();
    
            if (systolicBP > 180 || systolicBP < 90 || diastolicBP > 120 || diastolicBP < 60) {
                // Trigger critical threshold alert
                triggerAlert(new Alert("Critical Threshold Alert", "Critical blood pressure threshold exceeded."));
                return; // Alert triggered, no need to continue checking
            }
        }
    }
    
    
    //blood saturation alert
    static class checkBloodSaturationAlert(List<PatientRecord> records) {
        for (int i = 0; i < records.size(); i++) {
            PatientRecord currentRecord = records.get(i);
            double currentSaturation = currentRecord.getBloodSaturation();
            
            // Check for low saturation
            if (currentSaturation < 92) {
                triggerAlert(new Alert("Low Saturation Alert", "Blood oxygen saturation level falls below 92%."));
                return; // Alert triggered, no need to continue checking
            }
            
            // Check for rapid drop
            if (i >= 1) {
                PatientRecord previousRecord = records.get(i - 1);
                long timeDifference = currentRecord.getTimestamp() - previousRecord.getTimestamp();
                double previousSaturation = previousRecord.getBloodSaturation();
                
                // Calculate percentage drop
                double percentageDrop = ((previousSaturation - currentSaturation) / previousSaturation) * 100;
                
                // Check if the drop is more than 5% within a 10-minute interval
                if (percentageDrop >= 5 && timeDifference <= (10 * 60 * 1000)) {
                    triggerAlert(new Alert("Rapid Drop Alert", "Blood oxygen saturation level drops by 5% or more within a 10-minute interval."));
                    return; // Alert triggered, no need to continue checking
                }
            }
        }
    }
    
    
    //hypotensive hypoxemia alert
    public class checkHypotensiveHypoxemiaAlert(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            double systolicBP = record.getSystolicBloodPressure();
            double saturation = record.getBloodSaturation();
            
            if (systolicBP < 90 && saturation < 92) {
                triggerAlert(new Alert("Hypotensive Hypoxemia Alert", "Systolic blood pressure is below 90 mmHg and blood oxygen saturation falls below 92%."));
                return; // Alert triggered, no need to continue checking
            }
        }
    }
    
    
    // ECG Alerts
    public class checkECGAlert(List<PatientRecord> records) {
        
        for (PatientRecord record : records) {
            int heartRate = record.getHeartRate();
            boolean irregularBeat = record.isIrregularBeat();
    
            // Check for abnormal heart rate
            if (heartRate < 50 || heartRate > 100) {
                triggerAlert(new Alert("Abnormal Heart Rate Alert", "Heart rate is below 50 bpm or above 100 bpm."));
                return; // Alert triggered, no need to continue checking
            }
            
            // Check for irregular beat patterns
            if (irregularBeat) {
                AlertGenerator.triggerAlert(new Alert("Irregular Beat Alert", "Irregular beat pattern detected."));
                return; // Alert triggered, no need to continue checking
            }
        }
    }
    
}


    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("ALERT: " + alert.getType() + " - " + alert.getMessage());
    }
    
}
