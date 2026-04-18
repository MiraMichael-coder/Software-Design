package com.crm.reporting.model;

import com.crm.reporting.ReportRenderer;

// Bridge Pattern — Refined Abstraction for order details reports.
public class OrderReport extends Report {
    public OrderReport(String reportId, ReportRenderer renderer) {
        super(reportId, renderer);
    }

    @Override
    public void generate() {
        // Skeleton: no actual report generation
        renderer.render(reportId, "OrderReport");
    }
}
