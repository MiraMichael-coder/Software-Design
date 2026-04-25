package com.crm.reporting.model;

// Bridge Pattern — Refined Abstraction for customer summary reports.
public class CustomerSummaryReport extends Report {
    public CustomerSummaryReport(String reportId, ReportRenderer renderer) {
        super(reportId, renderer);
    }

    @Override
    protected void collectData() {
        System.out.println("[CustomerSummaryReport] Collecting customer metrics...");
    }

    @Override
    protected void formatData() {
        System.out.println("[CustomerSummaryReport] Formatting summary data...");
    }

    @Override
    protected String getReportType() {
        return "CustomerSummaryReport";
    }
}
