package com.crm.reporting;

// Bridge Pattern — Concrete Implementor for exporting reports to CSV.
public class CsvReportRenderer implements ReportRenderer {
    @Override
    public void render(String reportId, String reportType) {
        System.out.println("[ CSV ] Exporting " + reportType + ", ID: " + reportId + " to CSV");
    }
}
