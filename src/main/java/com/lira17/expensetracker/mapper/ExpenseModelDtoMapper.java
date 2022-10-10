package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.dto.create.ExpenseCreateDto;
import com.lira17.expensetracker.dto.get.ExpenseGetDto;
import com.lira17.expensetracker.model.Expense;
import com.lira17.expensetracker.service.ExpenseCategoryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class ExpenseModelDtoMapper extends BaseBalanceEntityMapper implements ModelDtoMapper<Expense, ExpenseGetDto, ExpenseCreateDto> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExpenseCategoryService expenseCategoryService;

    private static final Type LIST_OF_EXPENSES_TYPE = new TypeToken<List<ExpenseGetDto>>() {
    }.getType();


    @Override
    public ExpenseGetDto mapToDto(Expense model) {
        return modelMapper.map(model, ExpenseGetDto.class);
    }

    @Override
    public List<ExpenseGetDto> mapToDtoList(List<Expense> modelList) {
        return modelMapper.map(modelList, LIST_OF_EXPENSES_TYPE);
    }

    @Override
    public Expense mapToModel(ExpenseCreateDto dto) {
        var expense = modelMapper.map(dto, Expense.class);
        var category = expenseCategoryService.getCategoryById(dto.getCategory());
        expense.setCategory(category);
        populateBaseBalanceEntity(expense);
        return expense;
    }
}
