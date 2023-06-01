package com.lira17.expensetracker.report.model;

import lombok.Data;

import java.util.List;

@Data
public class FullReport extends Report {
    private List<ReportBalanceEntity> expenses;
    private List<ReportBalanceEntity> incomes;

    public FullReport(Report report) {
        super();
        super.setMonth(report.getMonth());
        super.setYear(report.getYear());
        super.setDifference(report.getDifference());
        super.setTotalExpense(report.getTotalExpense());
        super.setTotalIncome(report.getTotalIncome());
        super.setBalancePositive(report.isBalancePositive());
        super.setMainCurrency(report.getMainCurrency());
    }
}
