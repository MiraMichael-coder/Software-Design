package com.crm.reporting.factory;

import com.crm.reporting.CustomerSummaryReport;
import com.crm.reporting.Report;

public class CustomerReportFactory implements ReportFactory {
    @Override
    public Report createReport(String reportId) {
        return new CustomerSummaryReport(reportId);
    }
}
