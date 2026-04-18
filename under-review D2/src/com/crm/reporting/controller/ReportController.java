package com.crm.reporting.controller;

import com.crm.reporting.factory.ReportFactory;
import com.crm.reporting.model.Report;

public class ReportController {
    public Report generateReport(ReportFactory factory, String reportId) {
        Report report = factory.createReport(reportId);
        report.generate();
        return report;
    }
}
