package com.lira17.expensetracker.dto.get;

import com.lira17.expensetracker.common.IncomeCategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncomeCategoryGetDto {

    private long id;

    private String title;

    private IncomeCategoryType type;
}
