package com.crm.reporting.factory;

import com.crm.reporting.ReportRenderer;
import com.crm.reporting.model.Report;
import com.crm.reporting.model.WeeklyDeliveryReport;

public class WeeklyDeliveryReportFactory implements ReportFactory {
    private ReportRenderer renderer;

    public WeeklyDeliveryReportFactory(ReportRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Report createReport(String reportId) {
        return new WeeklyDeliveryReport(reportId, renderer);
    }
}
