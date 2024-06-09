package com.alerts;

import com.data_storage.Patient;

public interface AlertStrategy {
    void checkAlert(Patient patient);
}

