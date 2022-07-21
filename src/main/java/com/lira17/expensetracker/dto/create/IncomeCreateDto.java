package com.lira17.expensetracker.dto.create;

import com.lira17.expensetracker.exchange.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeCreateDto {

    private String title;

    private String description;

    private LocalDate date;

    private double amount;

    private Currency currency;

    CategoryCreateDto category;
}
