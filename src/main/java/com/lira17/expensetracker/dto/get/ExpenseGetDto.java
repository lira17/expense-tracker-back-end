package com.lira17.expensetracker.dto.get;

import com.lira17.expensetracker.exchange.Currency;

import java.time.LocalDate;

public record ExpenseGetDto(long id, String title, String description, LocalDate date, double amount, Currency currency,
                            Currency mainCurrency, double amountInMainCurrency, double rate,
                            ExpenseCategoryGetDto category) {
}
