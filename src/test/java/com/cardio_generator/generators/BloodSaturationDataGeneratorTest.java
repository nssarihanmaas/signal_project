package com.cardio_generator.generators;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cardio_generator.outputs.OutputStrategy;

import java.util.ArrayList;
import java.util.List;

public class BloodSaturationDataGeneratorTest {

    private BloodSaturationDataGenerator dataGenerator;
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
        dataGenerator = new BloodSaturationDataGenerator(PATIENT_COUNT);
        testOutputStrategy = new TestOutputStrategy();
    }

    @Test
    void testGenerateSaturation() {
        for (int i = 1; i <= PATIENT_COUNT; i++) {
            testOutputStrategy.clear();
            dataGenerator.generate(i, testOutputStrategy);
            boolean found = testOutputStrategy.getOutputs().stream()
                    .anyMatch(output -> output.contains("Saturation"));
            assertTrue(found, "Saturation data not generated for patient " + i);
        }
    }

    @Test
    void testGeneratedValuesWithinExpectedRange() {
        // Test
    }

    @Test
    void testGenerateInvalidPatientId() {
        int invalidPatientId = PATIENT_COUNT + 1; // Out of bounds ID
        testOutputStrategy.clear();
        dataGenerator.generate(invalidPatientId, testOutputStrategy);
        assertTrue(testOutputStrategy.getOutputs().isEmpty(), "Output generated for invalid patient ID");
    }
}
