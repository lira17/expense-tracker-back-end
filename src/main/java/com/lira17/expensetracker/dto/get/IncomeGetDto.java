package com.lira17.expensetracker.dto.get;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeGetDto extends BaseBalanceEntityGetDto {
    IncomeCategoryGetDto category;
}
