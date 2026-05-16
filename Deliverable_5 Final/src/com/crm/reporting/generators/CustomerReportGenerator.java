package com.crm.reporting.generators;

import com.crm.reporting.model.CustomerSummaryReport;
import com.crm.reporting.model.Report;
import com.crm.reporting.model.ReportRenderer;

public class CustomerReportGenerator implements ReportGenerator {
    private ReportRenderer renderer;

    public CustomerReportGenerator(ReportRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Report createReport(String reportId) {
        return new CustomerSummaryReport(reportId, renderer);
    }
}
