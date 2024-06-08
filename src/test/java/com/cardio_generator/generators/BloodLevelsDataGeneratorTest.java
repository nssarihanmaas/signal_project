package com.cardio_generator.generators;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cardio_generator.outputs.OutputStrategy;

import java.util.ArrayList;
import java.util.List;

public class BloodLevelsDataGeneratorTest {

    private BloodLevelsDataGenerator dataGenerator;
    private static final int PATIENT_COUNT = 10;

    private static class TestOutputStrategy implements OutputStrategy {
        private List<String> outputs = new ArrayList<>();

        @Override
        public void output(int patientId, long timestamp, String dataType, String value) {
            outputs.add(patientId + "," + timestamp + "," + dataType + "," + value);
        }

        public List<String> getOutputs() {
            return outputs;
        }

        public void clear() {
            outputs.clear();
        }
    }

    private TestOutputStrategy testOutputStrategy;

    @BeforeEach
    void setUp() {
        dataGenerator = new BloodLevelsDataGenerator(PATIENT_COUNT);
        testOutputStrategy = new TestOutputStrategy();
    }

    @Test
    void testGenerateCholesterol() {
        for (int i = 1; i <= PATIENT_COUNT; i++) {
            testOutputStrategy.clear();
            dataGenerator.generate(i, testOutputStrategy);
            boolean found = testOutputStrategy.getOutputs().stream()
                    .anyMatch(output -> output.contains("Cholesterol"));
            assertTrue(found, "Cholesterol data not generated for patient " + i);
        }
    }

    @Test
    void testGenerateWhiteBloodCells() {
        for (int i = 1; i <= PATIENT_COUNT; i++) {
            testOutputStrategy.clear();
            dataGenerator.generate(i, testOutputStrategy);
            boolean found = testOutputStrategy.getOutputs().stream()
                    .anyMatch(output -> output.contains("WhiteBloodCells"));
            assertTrue(found, "WhiteBloodCells data not generated for patient " + i);
        }
    }

    @Test
    void testGenerateRedBloodCells() {
        for (int i = 1; i <= PATIENT_COUNT; i++) {
            testOutputStrategy.clear();
            dataGenerator.generate(i, testOutputStrategy);
            boolean found = testOutputStrategy.getOutputs().stream()
                    .anyMatch(output -> output.contains("RedBloodCells"));
            assertTrue(found, "RedBloodCells data not generated for patient " + i);
        }
    }

    @Test
    void testGeneratedValuesWithinExpectedRange() {
        for (int i = 1; i <= PATIENT_COUNT; i++) {
            testOutputStrategy.clear();
            dataGenerator.generate(i, testOutputStrategy);
            for (String output : testOutputStrategy.getOutputs()) {
                String[] parts = output.split(",");
                double value = Double.parseDouble(parts[3]);
                switch (parts[2]) { // dataType
                    case "Cholesterol":
                        assertTrue(value >= 140 && value <= 200, "Cholesterol value out of range for patient " + i);
                        break;
                    case "WhiteBloodCells":
                        assertTrue(value >= 3.5 && value <= 10, "WhiteBloodCells value out of range for patient " + i);
                        break;
                    case "RedBloodCells":
                        assertTrue(value >= 4.3 && value <= 6, "RedBloodCells value out of range for patient " + i);
                        break;
                }
            }
        }
    }

    @Test
    void testGenerateInvalidPatientId() {
        int invalidPatientId = PATIENT_COUNT + 1;
        testOutputStrategy.clear();
        dataGenerator.generate(invalidPatientId, testOutputStrategy);
        assertTrue(testOutputStrategy.getOutputs().isEmpty(), "Output generated for invalid patient ID");
    }
}
