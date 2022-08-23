package com.lira17.expensetracker.report.model;

import com.lira17.expensetracker.dto.get.CategoryGetDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReportBalanceEntity {
    private CategoryGetDto category;
    private double categoryTotalAmount;
    private double categoryPercent;
}
