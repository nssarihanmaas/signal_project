package com.alerts;

public class PriorityAlertDecorator extends AlertDecorator {
    private int priority;

    public PriorityAlertDecorator(Alert decoratedAlert, int priority) {
        super(decoratedAlert);
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}