package com.lira17.expensetracker.dto.get;

import com.lira17.expensetracker.common.IncomeCategoryType;


public record IncomeCategoryGetDto(long id, String title, IncomeCategoryType type) {
}
