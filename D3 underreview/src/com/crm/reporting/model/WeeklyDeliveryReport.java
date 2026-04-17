package com.crm.reporting.model;

// Bridge Pattern — Refined Abstraction for weekly delivery performance reports.
public class WeeklyDeliveryReport extends Report {
    public WeeklyDeliveryReport(String reportId, ReportRenderer renderer) {
        super(reportId, renderer);
    }

    @Override
    protected void collectData() {
        System.out.println("[WeeklyDeliveryReport] Collecting delivery records for the week...");
    }

    @Override
    protected void formatData() {
        System.out.println("[WeeklyDeliveryReport] Formatting delivery statistics...");
    }

    @Override
    protected String getReportType() {
        return "WeeklyDeliveryReport";
    }
}
