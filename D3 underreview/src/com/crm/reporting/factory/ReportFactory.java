package com.crm.reporting.factory;

import com.crm.reporting.model.Report;

public interface ReportFactory {
    Report createReport(String reportId);
}
