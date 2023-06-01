package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.dto.create.ExpenseCreateDto;
import com.lira17.expensetracker.dto.get.ExpenseGetDto;
import com.lira17.expensetracker.model.Expense;
import com.lira17.expensetracker.service.ExpenseCategoryService;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public abstract class ExpenseMapper {

    @Autowired
    ExpenseCategoryService expenseCategoryService;

    @Autowired
    BaseBalanceEntityMapper baseBalanceEntityMapper;

    public abstract ExpenseGetDto convertToDto(Expense expense);

    public abstract List<ExpenseGetDto> convertToDto(Iterable<Expense> expenses);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "month", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "mainCurrency", ignore = true)
    @Mapping(target = "amountInMainCurrency", ignore = true)
    @Mapping(target = "rate", ignore = true)
    @Mapping(target = "category", expression = "java(expenseCategoryService.getCategoryById(expenseCreateDto.category().id()))")
    public abstract Expense convertToExpense(ExpenseCreateDto expenseCreateDto);

    @AfterMapping
    public void updateResult(ExpenseCreateDto expenseCreateDto, @MappingTarget Expense expense) {
        baseBalanceEntityMapper.populateBaseBalanceEntity(expense);
    }
}
