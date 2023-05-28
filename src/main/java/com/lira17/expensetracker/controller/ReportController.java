package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.report.model.FullReport;
import com.lira17.expensetracker.report.model.Report;
import com.lira17.expensetracker.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static com.lira17.expensetracker.common.Constants.API;

@RestController
@Tag(name = "Report", description = "Report API")
@RequestMapping(API + "report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/month")
    @Operation(summary = "Get expenses and incomes report for month/year")
    public FullReport getFullReportForMonth(@RequestParam int month, @RequestParam int year) {
        return reportService.getFullReportForMonth(month, year);
    }

    @GetMapping(value = "/year")
    @Operation(summary = "Get expenses and incomes report for month/year")
    public FullReport getFullReportForYear(@RequestParam int year) {
        return reportService.getFullReportForYear(year);
    }

    @GetMapping(value = "/year/months")
    @Operation(summary = "Get expenses and incomes report for all months of the year")
    public List<Report> getMonthsReportsForYearReport(@RequestParam int year) {
        return IntStream.range(1, getLastMonthValue(year))
                .mapToObj(month -> reportService.getReportForMonth(month, year))
                .toList();
    }

    private int getLastMonthValue(int year) {
        var localDateNow = LocalDate.now();
        var currentYear = localDateNow.getYear();
        return year < currentYear ? 13 : localDateNow.getMonthValue() + 1;
    }
}
