package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.dto.create.IncomeCreateDto;
import com.lira17.expensetracker.dto.get.IncomeGetDto;
import com.lira17.expensetracker.model.Income;
import com.lira17.expensetracker.service.IncomeCategoryService;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public abstract class IncomeMapper {

    @Autowired
    IncomeCategoryService incomeCategoryService;

    @Autowired
    BaseBalanceEntityMapper baseBalanceEntityMapper;

    public abstract IncomeGetDto convertToDto(Income income);

    public abstract List<IncomeGetDto> convertToDto(Iterable<Income> incomes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "month", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "mainCurrency", ignore = true)
    @Mapping(target = "amountInMainCurrency", ignore = true)
    @Mapping(target = "rate", ignore = true)
    @Mapping(target = "category", expression = "java(incomeCategoryService.getCategoryById(incomeCreateDto.category().id()))")
    public abstract Income convertToIncome(IncomeCreateDto incomeCreateDto);

    @AfterMapping
    public void updateResult(IncomeCreateDto incomeCreateDto, @MappingTarget Income income) {
        baseBalanceEntityMapper.populateBaseBalanceEntity(income);
    }
}
