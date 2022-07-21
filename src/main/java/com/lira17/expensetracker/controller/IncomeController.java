package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.dto.create.IncomeCreateDto;
import com.lira17.expensetracker.dto.get.IncomeGetDto;
import com.lira17.expensetracker.mapper.IncomeModelDtoMapper;
import com.lira17.expensetracker.service.IncomeService;
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

import java.util.List;

import static com.lira17.expensetracker.common.Constants.API;

@RestController
@RequestMapping(API + "incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private IncomeModelDtoMapper mapper;


    @GetMapping
    public List<IncomeGetDto> getAllIncomes() {
        return mapper.mapToDtoList(incomeService.getAllIncomes());
    }

    @GetMapping(value = "/{id}")
    public IncomeGetDto getIncomeById(@PathVariable("id") Long id) {
        return mapper.mapToDto(incomeService.getIncomeById(id));
    }

    @PostMapping
    public IncomeGetDto createIncome(@RequestBody IncomeCreateDto incomeCreateDto) {
        var income = mapper.mapToModel(incomeCreateDto);
        return mapper.mapToDto(incomeService.addIncome(income));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteIncome(@PathVariable("id") Long id) {
        incomeService.deleteIncome(id);
    }
}
