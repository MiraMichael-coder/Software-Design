package com.crm.alert.model;

// Decorator Pattern — Concrete Decorator #3: Retry.
public class RetryAlertDecorator extends AlertDecorator {

    private final int maxRetries;

    public RetryAlertDecorator(SystemAlert alert, int maxRetries) {
        super(alert);
        this.maxRetries = maxRetries;
    }

    // Convenience constructor — defaults to 3 retries.
    public RetryAlertDecorator(SystemAlert alert) {
        this(alert, 3);
    }

    @Override
    public void notifyTarget() {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            System.out.println("[ RETRY ] Attempt " + attempt + "/" + maxRetries
                    + " for alert " + alertId);
            try {
                super.notifyTarget(); // delegate to wrapped alert
                System.out.println("[ RETRY ] Alert " + alertId
                        + " succeeded on attempt " + attempt + ".");
                return; // success — stop retrying
            } catch (Exception e) {
                System.out.println("[ RETRY ] Attempt " + attempt
                        + " failed: " + e.getMessage());
            }
        }
        System.out.println("[ RETRY ] All " + maxRetries
                + " attempts exhausted for alert " + alertId + ".");
    }
}
