package com.lira17.expensetracker.report.model;

import lombok.Data;

import java.util.List;

@Data
public class Report {
    private int year;
    private int month;
    private double totalIncome;
    private double totalExpense;
    private boolean isBalancePositive;
    private double difference;
    private List<ReportBalanceEntity> expenses;
    private List<ReportBalanceEntity> incomes;
}
