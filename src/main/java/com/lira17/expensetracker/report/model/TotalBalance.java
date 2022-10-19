package com.lira17.expensetracker.report.model;

import com.lira17.expensetracker.exchange.Currency;

public record TotalBalance(double balanceAmount, Currency mainCurrency, boolean isBalancePositive) {
}
