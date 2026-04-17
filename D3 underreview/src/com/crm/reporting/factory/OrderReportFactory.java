package com.crm.reporting.factory;

import com.crm.reporting.model.OrderReport;
import com.crm.reporting.model.Report;
import com.crm.reporting.model.ReportRenderer;

public class OrderReportFactory implements ReportFactory {
    private ReportRenderer renderer;

    public OrderReportFactory(ReportRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Report createReport(String reportId) {
        return new OrderReport(reportId, renderer);
    }
}
