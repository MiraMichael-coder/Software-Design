package com.crm.reporting.controller;

import com.crm.reporting.generators.ReportGenerator;
import com.crm.reporting.model.Report;

public class ReportController {
    public Report generateReport(ReportGenerator factory, String reportId) {
        Report report = factory.createReport(reportId);
        report.generate();
        return report;
    }
}
