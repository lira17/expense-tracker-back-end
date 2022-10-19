package com.lira17.expensetracker.dto.create;

import com.lira17.expensetracker.exchange.Currency;

import java.time.LocalDate;


public record IncomeCreateDto(String title, String description, LocalDate date, double amount, Currency currency,
                              int category) {
}
