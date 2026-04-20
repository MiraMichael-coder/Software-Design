package com.crm.reporting.generators;

import com.crm.reporting.model.Report;

public interface ReportGenerator {
    Report createReport(String reportId);
}
