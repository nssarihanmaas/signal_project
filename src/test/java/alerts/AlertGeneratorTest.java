package alerts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_storage.DataStorage;
import com.data_storage.Patient;
import com.data_storage.PatientRecord;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import com.cardio_generator.generators.AlertGenerator;

public class AlertGeneratorTest {

    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    public void setUp() {
        dataStorage = mock(DataStorage.class);
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    public void testEvaluateData_noAlerts() {
        Patient patient = mock(Patient.class);
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 130, "BloodPressure", System.currentTimeMillis()),
                new PatientRecord(1, 120, "BloodPressure",  System.currentTimeMillis())
        );

        when(patient.getAllRecords()).thenReturn(records);

        alertGenerator.evaluateData(patient);

        // No alerts expected
        verify(patient, times(1)).getAllRecords();
    }

    @Test
    public void testEvaluateData_withAlerts() {
        Patient patient = mock(Patient.class);
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 150, "BloodPressure", System.currentTimeMillis()),
                new PatientRecord(1, 160, "BloodPressure",System.currentTimeMillis())
        );

        when(patient.getAllRecords()).thenReturn(records);

        alertGenerator.evaluateData(patient);

        // We expect 2 alerts to be triggered
        verify(patient, times(1)).getAllRecords();
        // Verify that triggerAlert is called twice
        // Assuming triggerAlert can be mocked or spied on
    }
}
