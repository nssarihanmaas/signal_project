package data_management;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import com.data_access.DataParser;
import com.data_storage.DataStorage;
import com.data_storage.PatientRecord;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);
        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
    }

    @Test
    void testParseValidData() {
        String data = "1,120.0,BloodPressure,170000000000";
        PatientRecord record = DataParser.parse(data);
        assertNotNull(record);
        assertEquals(1, record.getPatientId());
        assertEquals(120.0, record.getMeasurementValue());
        assertEquals("BloodPressure", record.getRecordType());
        assertEquals(170000000000L, record.getTimestamp()); // Validate first record
    }

    @Test
    void testParseInvalidData() {
        String invalidData = "invalid,data,format";
        PatientRecord record = DataParser.parse(invalidData);
        assertNull(record); // Check that parsing invalid data returns null
    }

    @Test
    void testAddAndRetrieveNoRecords() {
        DataStorage storage = DataStorage.getInstance();
        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(4, records.size()); // Ensure no records are returned for empty storage
    }

    @Test
    void testAddMultiplePatients() {
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(2, 200.0, "BloodPressure", 1714376789051L);
        
        List<PatientRecord> patient1Records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        List<PatientRecord> patient2Records = storage.getRecords(2, 1714376789050L, 1714376789051L);
        
        assertEquals(4, patient1Records.size());
        assertEquals(100.0, patient1Records.get(0).getMeasurementValue());

        assertEquals(1, patient2Records.size());
        assertEquals(200.0, patient2Records.get(0).getMeasurementValue());
    }

    @Test
    void testAddAndRetrieveSpecificRange() {
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 150.0, "WhiteBloodCells", 1714376789055L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789060L);
        
        List<PatientRecord> records = storage.getRecords(1, 1714376789051L, 1714376789059L);
        assertEquals(2, records.size()); // Ensure only one record is returned in the specified range
        assertEquals(200.0, records.get(0).getMeasurementValue());
    }

    @Test
    void testParseValidDataWithDifferentFormat() {
        String data = "2,85.0,HeartRate,172000000000";
        PatientRecord record = DataParser.parse(data);
        assertNotNull(record);
        assertEquals(2, record.getPatientId());
        assertEquals(85.0, record.getMeasurementValue());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(172000000000L, record.getTimestamp());
    }

}
