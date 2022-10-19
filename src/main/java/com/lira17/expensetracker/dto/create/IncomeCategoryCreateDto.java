package com.lira17.expensetracker.dto.create;

import com.lira17.expensetracker.common.IncomeCategoryType;

public record IncomeCategoryCreateDto(String title, IncomeCategoryType type) {
}
