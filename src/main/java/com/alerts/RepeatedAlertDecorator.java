package com.alerts;

public class RepeatedAlertDecorator extends AlertDecorator {
    public RepeatedAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
    }

    public void repeatAlert() {
        // Additional functionality to repeat the alert
        System.out.println("Repeating alert for patient ID: " + decoratedAlert.getPatientId());
    }
}
