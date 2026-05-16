package com.crm.reporting.generators;

import com.crm.reporting.model.Report;
import com.crm.reporting.model.ReportRenderer;
import com.crm.reporting.model.WeeklyDeliveryReport;

public class WeeklyDeliveryReportGenerator implements ReportGenerator {
    private ReportRenderer renderer;

    public WeeklyDeliveryReportGenerator(ReportRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Report createReport(String reportId) {
        return new WeeklyDeliveryReport(reportId, renderer);
    }
}
