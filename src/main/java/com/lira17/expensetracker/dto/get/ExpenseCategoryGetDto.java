package com.lira17.expensetracker.dto.get;

import com.lira17.expensetracker.common.ExpenseCategoryType;


public record ExpenseCategoryGetDto(long id, String title, ExpenseCategoryType type) {
}
