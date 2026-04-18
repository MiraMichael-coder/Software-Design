package com.crm.reporting.model;

import java.time.LocalDateTime;
import com.crm.reporting.ReportRenderer;

// Bridge Pattern — Abstraction for the report system, decoupled from its rendering logic.
public abstract class Report {
    protected String reportId;
    protected LocalDateTime generatedAt;
    protected ReportRenderer renderer;

    public Report(String reportId, ReportRenderer renderer) {
        this.reportId = reportId;
        this.generatedAt = LocalDateTime.now();
        this.renderer = renderer;
    }

    public String getReportId() {
        return reportId;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public abstract void generate();
}
