package com.crm.reporting.model;

// Bridge Pattern — Refined Abstraction for order details reports.
public class OrderReport extends Report {
    public OrderReport(String reportId, ReportRenderer renderer) {
        super(reportId, renderer);
    }

    @Override
    protected void collectData() {
        System.out.println("[OrderReport] Collecting order records...");
    }

    @Override
    protected void formatData() {
        System.out.println("[OrderReport] Formatting order data...");
    }

    @Override
    protected String getReportType() {
        return "OrderReport";
    }
}
