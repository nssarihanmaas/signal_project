package data_management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_access.DataListener;
import com.data_access.DataParser;
import com.data_access.FileDataListener;
import com.data_access.TCPDataListener;
import com.data_storage.DataStorage;
import com.data_storage.PatientRecord;

public class DataAccessTest {

    private MockDataListener dataListener;
    private MockDataStorage dataStorage;
    private File tempFile;
    private MockSocket socket;
    private MockBufferedReader reader;

    @BeforeEach
    void setUp() throws IOException {
        dataListener = new MockDataListener();
        dataStorage = new MockDataStorage();
        tempFile = File.createTempFile("testData", ".txt");
    }
    

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void testStartListening() {
        dataListener.startListening();
        assertTrue(dataListener.isListening());
    }

    @Test
    void testStopListening() {
        dataListener.stopListening();
        assertFalse(dataListener.isListening());
    }

    @Test
    void testOnDataReceived() {
        String testData = "Test data";
        dataListener.onDataReceived(testData);
        assertEquals(testData, dataListener.getLastReceivedData());
    }

    @Test
    void testOnDataReceivedWithNullData() {
        dataListener.onDataReceived(null);
        assertNull(dataListener.getLastReceivedData());
    }

    @Test
    void testParseValidData() {
        String data = "1,120.0,BloodPressure,170000000000";
        PatientRecord record = DataParser.parse(data);
        assertNotNull(record);
        assertEquals(1, record.getPatientId());
        assertEquals(120.0, record.getMeasurementValue());
        assertEquals("BloodPressure", record.getRecordType());
        assertEquals(170000000000L, record.getTimestamp());
    }

    @Test
    void testParseInvalidData() {
        String invalidData = "invalid,data,format";
        PatientRecord record = DataParser.parse(invalidData);
        assertNull(record);
    }

    @Test
    void testParseIncompleteData() {
        String incompleteData = "1,120.0,BloodPressure";
        PatientRecord record = DataParser.parse(incompleteData);
        assertNull(record);
    }

    @Test
    void testParseEmptyData() {
        String emptyData = "";
        PatientRecord record = DataParser.parse(emptyData);
        assertNull(record);
    }

    @Test
    void testParseDataWithExtraFields() {
        String extraData = "1,120.0,BloodPressure,170000000000,extra";
        PatientRecord record = DataParser.parse(extraData);
        assertNull(record);
    }

    @Test
    void testParseDataWithNonNumericFields() {
        String nonNumericData = "one,120.0,BloodPressure,170000000000";
        assertThrows(NumberFormatException.class, () -> {
            DataParser.parse(nonNumericData);
        });
    }

    @Test
    void testParseDataWithIncorrectMeasurementValue() {
        String incorrectMeasurementData = "1,abc,BloodPressure,170000000000";
        assertThrows(NumberFormatException.class, () -> {
            DataParser.parse(incorrectMeasurementData);
        });
    }

    @Test
    void testParseDataWithIncorrectTimestamp() {
        String incorrectTimestampData = "1,120.0,BloodPressure,abc";
        assertThrows(NumberFormatException.class, () -> {
            DataParser.parse(incorrectTimestampData);
        });
    }

    @Test
    void testFileDataListenerStartListening() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("1,120.0,BloodPressure,170000000000");
            writer.newLine();
            writer.write("2,130.0,HeartRate,170000000001");
        }

        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);
        fileDataListener.startListening();

        assertEquals(2, dataStorage.getRecords().size());
    }

    @Test
    void testFileDataListenerStopListening() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("1,120.0,BloodPressure,170000000000");
            writer.newLine();
            writer.write("2,130.0,HeartRate,170000000001");
        }

        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);

        Thread listenerThread = new Thread(() -> fileDataListener.startListening());
        listenerThread.start();

        // Stop listening after a short delay to simulate stopping the listener
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileDataListener.stopListening();

        try {
            listenerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that at least one record was processed before stopping
        assertTrue(dataStorage.getRecords().size() >= 1);
    }

    @Test
    void testFileDataListenerOnDataReceivedWithInvalidData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("invalid,data,format");
        }

        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);
        fileDataListener.startListening();

        // Verify that no data was added due to invalid format
        assertEquals(0, dataStorage.getRecords().size());
    }

    @Test
    void testFileDataListenerOnDataReceivedWithValidAndInvalidData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("1,120.0,BloodPressure,170000000000");
            writer.newLine();
            writer.write("invalid,data,format");
        }

        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);
        fileDataListener.startListening();

        assertEquals(1, dataStorage.getRecords().size());
    }

    @Test
    void testFileDataListenerStartListeningWithEmptyFile() throws IOException {
        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);
        fileDataListener.startListening();

        // Verify that no data was added
        assertEquals(0, dataStorage.getRecords().size());
    }

    @Test
    void testFileDataListenerOnDataReceivedWithNullData() {
        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);
        fileDataListener.onDataReceived(null);

        // Verify that no data was added
        assertEquals(0, dataStorage.getRecords().size());
    }

    @Test
    void testTCPDataListenerStartListening() throws IOException {
        MockSocket socket = new MockSocket();
        MockBufferedReader reader = new MockBufferedReader();
        reader.setLines(new String[]{
            "1,120.0,BloodPressure,170000000000",
            "2,130.0,HeartRate,170000000001",
            null
        });
        socket.setBufferedReader(reader);

        TCPDataListener tcpDataListener = new TCPDataListener(socket, dataStorage);
        tcpDataListener.startListening();

        assertEquals(2, dataStorage.getRecords().size());
    }

    // Mock Classes
    private static class MockDataListener implements DataListener {

        private boolean listening = false;
        private String lastReceivedData;

        @Override
        public void startListening() {
            listening = true;
        }

        @Override
        public void stopListening() {
            listening = false;
        }

        @Override
        public void onDataReceived(String data) {
            lastReceivedData = data;
        }

        public boolean isListening() {
            return listening;
        }

        public String getLastReceivedData() {
            return lastReceivedData;
        }
    }

    private static class MockDataStorage extends DataStorage {

        private List<PatientRecord> records = new ArrayList<>();

        @Override
        public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
            records.add(new PatientRecord(patientId, measurementValue, recordType, timestamp));
        }

        public List<PatientRecord> getRecords() {
            return records;
        }
    }

    private static class MockSocket extends Socket {

        private MockBufferedReader reader;

        public void setBufferedReader(MockBufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public InputStream getInputStream() {
            StringBuilder sb = new StringBuilder();
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ByteArrayInputStream(sb.toString().getBytes());
        }

    }

    private static class MockBufferedReader extends BufferedReader {

        private String[] lines;
        private int index = 0;

        public MockBufferedReader() {
            super(new InputStreamReader(System.in));
        }

        public void setLines(String[] lines) {
            this.lines = lines;
        }

        @Override
        public String readLine() throws IOException {
            if (index < lines.length) {
                return lines[index++];
            }
            return null;
        }
    }
}
