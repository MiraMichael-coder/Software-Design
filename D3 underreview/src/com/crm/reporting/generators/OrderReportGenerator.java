package com.crm.reporting.generators;

import com.crm.reporting.model.OrderReport;
import com.crm.reporting.model.Report;
import com.crm.reporting.model.ReportRenderer;

public class OrderReportGenerator implements ReportGenerator {
    private ReportRenderer renderer;

    public OrderReportGenerator(ReportRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Report createReport(String reportId) {
        return new OrderReport(reportId, renderer);
    }
}
