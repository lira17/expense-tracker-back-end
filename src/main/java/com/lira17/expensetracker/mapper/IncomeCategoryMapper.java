package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.dto.create.IncomeCategoryCreateDto;
import com.lira17.expensetracker.dto.get.IncomeCategoryGetDto;
import com.lira17.expensetracker.model.IncomeCategory;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface IncomeCategoryMapper {

    IncomeCategoryGetDto convertToDto(IncomeCategory incomeCategory);

    List<IncomeCategoryGetDto> convertToDto(Iterable<IncomeCategory> categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "incomes", ignore = true)
    IncomeCategory convertToCategory(IncomeCategoryCreateDto incomeCategoryCreateDto);
}
