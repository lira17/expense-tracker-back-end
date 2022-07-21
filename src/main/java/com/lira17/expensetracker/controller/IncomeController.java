package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.dto.create.IncomeCreateDto;
import com.lira17.expensetracker.dto.get.IncomeGetDto;
import com.lira17.expensetracker.mapper.IncomeCreateDtoMapper;
import com.lira17.expensetracker.service.IncomeService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

import static com.lira17.expensetracker.common.Constants.API;

@RestController
@RequestMapping(API + "incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IncomeCreateDtoMapper mapper;

    private static final Type LIST_OF_INCOMES_TYPE = new TypeToken<List<IncomeGetDto>>() {
    }.getType();

    @GetMapping
    public List<IncomeGetDto> getAllIncomes() {
        return modelMapper.map(incomeService.getAllIncomes(), LIST_OF_INCOMES_TYPE);
    }

    @GetMapping(value = "/{id}")
    public IncomeGetDto getIncomeById(@PathVariable("id") Long id) {
        return modelMapper.map(incomeService.getIncomeById(id), IncomeGetDto.class);
    }

    @PostMapping
    public IncomeGetDto createIncome(@RequestBody IncomeCreateDto incomeCreateDto) {
        var income = mapper.mapToModel(incomeCreateDto);
        return modelMapper.map(incomeService.addIncome(income), IncomeGetDto.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteIncome(@PathVariable("id") Long id) {
        incomeService.deleteIncome(id);
    }
}
