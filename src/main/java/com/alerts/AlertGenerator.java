package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
        
    public static void bloodPreassureAlert(List<String[]> records) {
    
        for (int i = 0; i < records.size(); i++) {
            int systolic = Integer.parseInt(records.get(i)[2]);
            int diastolic = Integer.parseInt(records.get(i)[3]);

        if (systolic > 180 || systolic < 90 || diastolic > 120 || diastolic < 60) {
            //triggerAlert
            }
    
        if (i < records.size() - 2) {
            int nextSystolic = Integer.parseInt(records.get(i + 1)[2]);
            int nextDiastolic = Integer.parseInt(records.get(i + 1)[3]);
            int nextNextSystolic = Integer.parseInt(records.get(i + 2)[2]);
            int nextNextDiastolic = Integer.parseInt(records.get(i + 2)[3]);
    
            boolean increasingTrend = (nextSystolic - systolic > 10 &&
                                        nextNextSystolic - nextSystolic > 10 &&
                                        nextDiastolic - diastolic > 10 &&
                                        nextNextDiastolic - nextDiastolic > 10);
    
            boolean decreasingTrend = (systolic - nextSystolic > 10 &&
                                        nextSystolic - nextNextSystolic > 10 &&
                                        diastolic - nextDiastolic > 10 &&
                                        nextDiastolic - nextNextDiastolic > 10);
    
        if (increasingTrend) {
            //triggerAlert
        } 
        else if (decreasingTrend) {
            //triggerAlert
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
        
    }
    
}
}
