package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_access.DataParser;
import com.data_storage.DataStorage;
import com.data_storage.PatientRecord;

import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        DataStorage storage = new DataStorage();
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

}
