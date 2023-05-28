package com.lira17.expensetracker.report.model;

import lombok.Data;


@Data
public class Report {
    private Integer year;
    private Integer month;
    private double totalIncome;
    private double totalExpense;
    private boolean isBalancePositive;
    private double difference;
}
