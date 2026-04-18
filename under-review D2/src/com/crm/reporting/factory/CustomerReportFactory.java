package com.crm.reporting.factory;

import com.crm.reporting.ReportRenderer;
import com.crm.reporting.model.CustomerSummaryReport;
import com.crm.reporting.model.Report;

public class CustomerReportFactory implements ReportFactory {
    private ReportRenderer renderer;

    public CustomerReportFactory(ReportRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Report createReport(String reportId) {
        return new CustomerSummaryReport(reportId, renderer);
    }
}
