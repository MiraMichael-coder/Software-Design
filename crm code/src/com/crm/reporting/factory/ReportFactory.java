package com.crm.reporting.factory;

import com.crm.reporting.Report;

public interface ReportFactory {
    Report createReport(String reportId);
}
