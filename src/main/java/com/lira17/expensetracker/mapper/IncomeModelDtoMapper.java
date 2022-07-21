package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.dto.create.IncomeCreateDto;
import com.lira17.expensetracker.dto.get.IncomeGetDto;
import com.lira17.expensetracker.model.Income;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class IncomeModelDtoMapper extends BaseBalanceEntityMapper implements ModelDtoMapper<Income, IncomeGetDto, IncomeCreateDto> {

    @Autowired
    ModelMapper modelMapper;

    private static final Type LIST_OF_INCOMES_TYPE = new TypeToken<List<IncomeGetDto>>() {
    }.getType();

    @Override
    public IncomeGetDto mapToDto(Income model) {
        return modelMapper.map(model, IncomeGetDto.class);
    }

    @Override
    public List<IncomeGetDto> mapToDtoList(List<Income> modelList) {
        return modelMapper.map(modelList, LIST_OF_INCOMES_TYPE);
    }

    @Override
    public Income mapToModel(IncomeCreateDto dto) {
        var income = modelMapper.map(dto, Income.class);
        populateBaseBalanceEntity(income);
        return income;
    }
}
