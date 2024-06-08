package data_management;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_storage.DataStorage;
import com.data_storage.PatientRecord;
import com.data_access.DataParser;
import com.data_access.FileDataListener;
import com.data_access.TCPDataListener;

public class DataAccessTest {

    private DataListener dataListener;
    private DataStorage dataStorage;
    private File tempFile;
    private Socket socket;
    private BufferedReader reader;

    @BeforeEach
    void setUp() throws IOException {
        dataListener = mock(DataListener.class);
        dataStorage = mock(DataStorage.class);
        tempFile = File.createTempFile("testData", ".txt");

        // Mocking Socket and BufferedReader for TCPDataListener
        socket = mock(Socket.class);
        reader = mock(BufferedReader.class);
        when(socket.getInputStream()).thenReturn(mock(InputStreamReader.class));
        when(new BufferedReader(new InputStreamReader(socket.getInputStream()))).thenReturn(reader);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void testStartListening() {
        dataListener.startListenig();
        verify(dataListener, times(1)).startListenig();
    }

    @Test
    void testStopListening() {
        dataListener.stopListening();
        verify(dataListener, times(1)).stopListening();
    }

    @Test
    void testOnDataReceived() {
        String testData = "Test data";
        dataListener.onDataReceived(testData);
        verify(dataListener, times(1)).onDataReceived(testData);
    }

    @Test
    void testStartListeningIsCalled() {
        dataListener.startListenig();
        assertDoesNotThrow(() -> verify(dataListener, times(1)).startListenig());
    }

    @Test
    void testStopListeningIsCalled() {
        dataListener.stopListening();
        assertDoesNotThrow(() -> verify(dataListener, times(1)).stopListening());
    }

    @Test
    void testOnDataReceivedIsCalledWithCorrectData() {
        String testData = "Another test data";
        dataListener.onDataReceived(testData);
        assertDoesNotThrow(() -> verify(dataListener, times(1)).onDataReceived(testData));
    }

    @Test
    void testOnDataReceivedWithNullData() {
        dataListener.onDataReceived(null);
        verify(dataListener, times(1)).onDataReceived(null);
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
        assertNull(record); // Check that parsing invalid data returns null
    }

    @Test
    void testParseIncompleteData() {
        String incompleteData = "1,120.0,BloodPressure";
        PatientRecord record = DataParser.parse(incompleteData);
        assertNull(record); // Check that parsing incomplete data returns null
    }

    @Test
    void testParseEmptyData() {
        String emptyData = "";
        PatientRecord record = DataParser.parse(emptyData);
        assertNull(record); // Check that parsing empty data returns null
    }

    @Test
    void testParseDataWithExtraFields() {
        String extraData = "1,120.0,BloodPressure,170000000000,extra";
        PatientRecord record = DataParser.parse(extraData);
        assertNull(record); // Check that parsing data with extra fields returns null
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
        fileDataListener.startListenig();

        verify(dataStorage, times(1)).addPatientData(1, 120.0, "BloodPressure", 170000000000L);
        verify(dataStorage, times(1)).addPatientData(2, 130.0, "HeartRate", 170000000001L);
    }

    @Test
    void testFileDataListenerStopListening() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("1,120.0,BloodPressure,170000000000");
            writer.newLine();
            writer.write("2,130.0,HeartRate,170000000001");
        }

        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);

        Thread listenerThread = new Thread(() -> fileDataListener.startListenig());
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
        verify(dataStorage, atLeast(1)).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    void testFileDataListenerOnDataReceivedWithInvalidData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("invalid,data,format");
        }

        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);
        fileDataListener.startListenig();

        // Verify that no data was added due to invalid format
        verify(dataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    void testFileDataListenerOnDataReceivedWithValidAndInvalidData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("1,120.0,BloodPressure,170000000000");
            writer.newLine();
            writer.write("invalid,data,format");
        }

        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);
        fileDataListener.startListenig();

        verify(dataStorage, times(1)).addPatientData(1, 120.0, "BloodPressure", 170000000000L);
        verify(dataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    void testFileDataListenerStartListeningWithEmptyFile() throws IOException {
        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);
        fileDataListener.startListenig();

        // Verify that no data was added
        verify(dataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    void testFileDataListenerOnDataReceivedWithNullData() {
        FileDataListener fileDataListener = new FileDataListener(tempFile.getAbsolutePath(), dataStorage);
        fileDataListener.onDataReceived(null);

        // Verify that no data was added
        verify(dataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    void testTCPDataListenerStartListening() throws IOException {
        when(reader.readLine())
            .thenReturn("1,120.0,BloodPressure,170000000000")
            .thenReturn("2,130.0,HeartRate,170000000001")
            .thenReturn(null);

        TCPDataListener tcpDataListener = new TCPDataListener("localhost", 8080, dataStorage);
        tcpDataListener.startListenig();

        verify(dataStorage, times(1)).addPatientData(1, 120.0, "BloodPressure", 170000000000L);
    }
}

