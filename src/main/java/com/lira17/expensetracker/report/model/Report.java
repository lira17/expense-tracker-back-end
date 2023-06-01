package com.lira17.expensetracker.report.model;

import com.lira17.expensetracker.exchange.Currency;
import lombok.Data;


@Data
public class Report {
    private Integer year;
    private Integer month;
    private double totalIncome;
    private double totalExpense;
    private boolean isBalancePositive;
    private double difference;
    private Currency mainCurrency;
}
