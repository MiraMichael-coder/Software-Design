package com.crm.reporting.model;

import java.time.LocalDateTime;

// Bridge Pattern — Abstraction for the report system, decoupled from its rendering logic.

//Tempelate pattern
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

    /*
     * It provides a consistent report generation workflow:
     * 1. Collect the raw data
     * 2. Format the collected data
     * 3. Render the final output via the renderer
     * Subclasses only implement collectData() and formatData() logic.
     */

    // Tempelate function
    public final void produceReport() {
        collectData();
        formatData();
        renderer.render(reportId, getReportType());
    }

    /** Entry point kept for backward compatibility — delegates to the workflow. */
    public void generate() {
        produceReport();
    }

    // Subclasses gather whatever raw data they need for the report.
    protected abstract void collectData();

    // Subclasses transform / structure the collected data before rendering.
    protected abstract void formatData();

    // Returns the human-readable type name used during rendering.
    protected abstract String getReportType();
}
