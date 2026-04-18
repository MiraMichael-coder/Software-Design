package com.crm.reporting;

public class CustomerSummaryReport extends Report {
    public CustomerSummaryReport(String reportId) {
        super(reportId);
    }

    @Override
    public void generate() {
        System.out.println("Generating CustomerSummaryReport: " + reportId);
    }
}
