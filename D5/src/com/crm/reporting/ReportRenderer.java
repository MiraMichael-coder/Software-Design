package com.crm.reporting;

// Bridge Pattern — Implementor interface for report rendering.
public interface ReportRenderer {
    void render(String reportId, String reportType);
}
