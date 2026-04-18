package com.crm.reporting;

public class OrderReport extends Report {
    public OrderReport(String reportId) {
        super(reportId);
    }

    @Override
    public void generate() {
        // Skeleton: no actual report generation
        System.out.println("Generating OrderReport: " + reportId);
    }
}
