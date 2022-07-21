package com.lira17.expensetracker.dto.get;

import com.lira17.expensetracker.common.ExpenseCategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCategoryGetDto {

    private long id;

    private String title;

    private ExpenseCategoryType type;
}
