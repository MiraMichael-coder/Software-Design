package com.crm.reporting;

import java.time.LocalDateTime;

public abstract class Report {
    protected String reportId;
    protected LocalDateTime generatedAt;

    public Report(String reportId) {
        this.reportId = reportId;
        this.generatedAt = LocalDateTime.now();
    }

    public String getReportId() { return reportId; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }

    public abstract void generate();
}
