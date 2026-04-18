package com.crm.reporting.factory;

import com.crm.reporting.Report;
import com.crm.reporting.WeeklyDeliveryReport;

public class WeeklyDeliveryReportFactory implements ReportFactory {
    @Override
    public Report createReport(String reportId) {
        return new WeeklyDeliveryReport(reportId);
    }
}
