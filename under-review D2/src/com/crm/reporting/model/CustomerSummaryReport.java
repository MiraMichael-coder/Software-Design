package com.crm.reporting.model;

import com.crm.reporting.ReportRenderer;

// Bridge Pattern — Refined Abstraction for customer summary reports.
public class CustomerSummaryReport extends Report {
    public CustomerSummaryReport(String reportId, ReportRenderer renderer) {
        super(reportId, renderer);
    }

    @Override
    public void generate() {
        renderer.render(reportId, "CustomerSummaryReport");
    }
}
