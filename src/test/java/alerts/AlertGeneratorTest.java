package alerts;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alerts.AlertGenerator;
import com.data_storage.DataStorage;
import com.data_storage.Patient;
import com.data_storage.PatientRecord;


public class AlertGeneratorTest {

    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    public void setUp() {
        dataStorage = DataStorage.getInstance();
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    public void testEvaluateData_noAlerts() {
        Patient patient = new Patient(0);
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(0, 130, "BloodPressure", System.currentTimeMillis()),
                new PatientRecord(0, 120, "BloodPressure",  System.currentTimeMillis())
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
        Patient patient = new Patient(1);
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
