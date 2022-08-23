package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.report.model.Report;
import com.lira17.expensetracker.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.lira17.expensetracker.common.Constants.API;

@RestController
@Tag(name = "Report", description = "Report API")
@RequestMapping(API + "report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    @Operation(summary = "Get expenses and incomes report for month/year")
    public Report getReport(@RequestParam(required = false) String month, @RequestParam String year) {
        return reportService.getReport(getMonthParam(month), Integer.parseInt(year));
    }

    private Integer getMonthParam(String month) {
        return StringUtils.isNotEmpty(month)
                ? Integer.parseInt(month)
                : null;
    }
}
