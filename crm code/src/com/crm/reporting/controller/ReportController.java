package com.crm.reporting.controller;

import com.crm.reporting.Report;
import com.crm.reporting.factory.ReportFactory;

public class ReportController {
    public Report generateReport(ReportFactory factory, String reportId) {
        Report report = factory.createReport(reportId);
        report.generate();
        return report;
    }
}
