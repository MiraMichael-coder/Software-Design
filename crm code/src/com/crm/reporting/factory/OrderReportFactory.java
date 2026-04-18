package com.crm.reporting.factory;

import com.crm.reporting.OrderReport;
import com.crm.reporting.Report;

public class OrderReportFactory implements ReportFactory {
    @Override
    public Report createReport(String reportId) {
        return new OrderReport(reportId);
    }
}
