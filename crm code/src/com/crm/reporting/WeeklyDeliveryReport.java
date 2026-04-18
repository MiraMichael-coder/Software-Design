package com.crm.reporting;

public class WeeklyDeliveryReport extends Report {
    public WeeklyDeliveryReport(String reportId) {
        super(reportId);
    }

    @Override
    public void generate() {
        System.out.println("Generating WeeklyDeliveryReport: " + reportId);
    }
}
