package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.dto.create.IncomeCreateDto;
import com.lira17.expensetracker.dto.get.IncomeGetDto;
import com.lira17.expensetracker.mapper.IncomeModelDtoMapper;
import com.lira17.expensetracker.model.Income;
import com.lira17.expensetracker.service.IncomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Tag(name = "Income", description = "Income API")
@RequestMapping(API + "incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private IncomeModelDtoMapper mapper;


    @GetMapping
    @Operation(summary = "Get all incomes")
    public List<IncomeGetDto> getAllIncomes(@ParameterObject Pageable pageable) {
        Page<Income> allIncomes = incomeService.getAllIncomes(pageable);
        return mapper.mapToDtoList(allIncomes.getContent());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get income by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Income is found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = IncomeGetDto.class))}),
            @ApiResponse(responseCode = "404", description = "Income is not found", content = @Content)})
    public IncomeGetDto getIncomeById(@PathVariable("id") Long id) {
        return mapper.mapToDto(incomeService.getIncomeById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an income")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Income is created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = IncomeGetDto.class))}),
            @ApiResponse(responseCode = "404", description = "Income category is not found", content = @Content)})
    public IncomeGetDto createIncome(@RequestBody IncomeCreateDto incomeCreateDto) {
        var income = mapper.mapToModel(incomeCreateDto);
        return mapper.mapToDto(incomeService.addIncome(income));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete income by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "Income is not found", content = @Content)})
    public void deleteIncome(@PathVariable("id") Long id) {
        incomeService.deleteIncome(id);
    }
}
