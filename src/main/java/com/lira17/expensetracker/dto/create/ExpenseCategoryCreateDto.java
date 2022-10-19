package com.lira17.expensetracker.dto.create;

import com.lira17.expensetracker.common.ExpenseCategoryType;


public record ExpenseCategoryCreateDto(String title, ExpenseCategoryType type) {
}
