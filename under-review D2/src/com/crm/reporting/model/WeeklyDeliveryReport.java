package com.crm.reporting.model;

import com.crm.reporting.ReportRenderer;

// Bridge Pattern — Refined Abstraction for weekly delivery performance reports.
public class WeeklyDeliveryReport extends Report {
    public WeeklyDeliveryReport(String reportId, ReportRenderer renderer) {
        super(reportId, renderer);
    }

    @Override
    public void generate() {
        renderer.render(reportId, "WeeklyDeliveryReport");
    }
}
