package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.dto.create.ExpenseCategoryCreateDto;
import com.lira17.expensetracker.dto.get.ExpenseCategoryGetDto;
import com.lira17.expensetracker.model.ExpenseCategory;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface ExpenseCategoryMapper {

    ExpenseCategoryGetDto convertToDto(ExpenseCategory expenseCategory);

    List<ExpenseCategoryGetDto> convertToDto(Iterable<ExpenseCategory> categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expenses", ignore = true)
    ExpenseCategory convertToCategory(ExpenseCategoryCreateDto expenseCategoryCreateDto);
}
