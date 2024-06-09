package alerts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cardio_generator.data_storage.DataStorage;
import com.cardio_generator.Patient;
import com.data_storage.PatientRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Import the AlertGenerator class
import com.cardio_generator.generators.AlertGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataStorage {
    // Assuming DataStorage has some storage mechanism
}

class Patient {
    private List<PatientRecord> records;
    private int alertCount;

    public Patient() {
        this.records = new ArrayList<>();
        this.alertCount = 0;
    }

    public void addRecord(PatientRecord record) {
        this.records.add(record);
    }

    public List<PatientRecord> getAllRecords() {
        return this.records;
    }

    public void triggerAlert() {
        this.alertCount++;
    }

    public int getAlertCount() {
        return alertCount;
    }
}

class PatientRecord {
    private int patientId;
    private int value;
    private String type;
    private long timestamp;

    public PatientRecord(int patientId, int value, String type, long timestamp) {
        this.patientId = patientId;
        this.value = value;
        this.type = type;
        this.timestamp = timestamp;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

public class AlertGeneratorTest {

    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    public void setUp() {
        dataStorage = new DataStorage();
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    public void testEvaluateData_noAlerts() {
        Patient patient = new Patient();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 130, "BloodPressure", System.currentTimeMillis()),
                new PatientRecord(1, 120, "BloodPressure",  System.currentTimeMillis())
        );

        for (PatientRecord record : records) {
            patient.addRecord(record);
        }

        alertGenerator.evaluateData(patient);

        // No alerts expected
        assertEquals(0, patient.getAlertCount());
    }

    @Test
    public void testEvaluateData_withAlerts() {
        Patient patient = new Patient();
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 150, "BloodPressure", System.currentTimeMillis()),
                new PatientRecord(1, 160, "BloodPressure", System.currentTimeMillis())
        );

        for (PatientRecord record : records) {
            patient.addRecord(record);
        }

        alertGenerator.evaluateData(patient);

        // We expect 2 alerts to be triggered
        assertEquals(2, patient.getAlertCount());
    }
}
