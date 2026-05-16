package com.crm.reporting.model;

// Bridge Pattern — Concrete Implementor for exporting reports to CSV.
public class CsvReportRenderer implements ReportRenderer {
    @Override
    public void render(String reportId, String reportType) {
        System.out.println("Exporting " + reportType + ", ID: " + reportId + " to CSV");
    }
}
