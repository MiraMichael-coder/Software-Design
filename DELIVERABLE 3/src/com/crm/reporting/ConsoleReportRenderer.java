package com.crm.reporting;

// Bridge Pattern — Concrete Implementor for rendering reports to the console.
public class ConsoleReportRenderer implements ReportRenderer {
    @Override
    public void render(String reportId, String reportType) {
        System.out.println("[ CONSOLE ] " + reportType + " - ID: " + reportId);
    }
}
