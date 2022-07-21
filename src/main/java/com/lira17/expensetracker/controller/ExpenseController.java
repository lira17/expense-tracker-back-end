package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.dto.create.ExpenseCreateDto;
import com.lira17.expensetracker.dto.get.ExpenseGetDto;
import com.lira17.expensetracker.mapper.ExpenseCreateDtoMapper;
import com.lira17.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping(API + "expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExpenseCreateDtoMapper mapper;

    private static final Type LIST_OF_EXPENSES_TYPE = new TypeToken<List<ExpenseGetDto>>() {
    }.getType();

    @GetMapping
    public List<ExpenseGetDto> getAllExpenses() {
        return modelMapper.map(expenseService.getAllExpenses(), LIST_OF_EXPENSES_TYPE);
    }

    @GetMapping(value = "/{id}")
    @ApiResponses(value = {
           /* @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseGetDto.class)) }),*/
            @ApiResponse(responseCode = "404", description = "Expense not found",
                    content = @Content) })
    public ExpenseGetDto getExpenseById(@PathVariable("id") Long id) {
        return modelMapper.map(expenseService.getExpenseById(id), ExpenseGetDto.class);
    }

    @PostMapping
    public ExpenseGetDto createExpense(@RequestBody ExpenseCreateDto expenseCreateDto) {
        var expense = mapper.mapToModel(expenseCreateDto);
        return modelMapper.map(expenseService.addExpense(expense), ExpenseGetDto.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteExpense(@PathVariable("id") Long id) {
        expenseService.deleteExpense(id);
    }
}
