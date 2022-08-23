package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.dto.create.ExpenseCategoryCreateDto;
import com.lira17.expensetracker.dto.get.ExpenseCategoryGetDto;
import com.lira17.expensetracker.exchange.ExchangeService;
import com.lira17.expensetracker.model.ExpenseCategory;
import com.lira17.expensetracker.service.ExpenseCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Expense Category", description = "Expense Category API")
@RequestMapping(API + "expense-categories")
public class ExpenseCategoryController {

    @Autowired
    private ExpenseCategoryService categoryService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private ModelMapper modelMapper;

    private static final Type LIST_OF_CATEGORIES_TYPE = new TypeToken<List<ExpenseCategoryGetDto>>() {
    }.getType();

    @GetMapping
    @Operation(summary = "Get all expense categories")
    public List<ExpenseCategoryGetDto> getAllCategories() {
        return modelMapper.map(categoryService.getAllCategories(), LIST_OF_CATEGORIES_TYPE);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get expense category by id")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense category is found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExpenseCategoryGetDto.class))}),
            @ApiResponse(responseCode = "404", description = "Expense category is not found", content = @Content)})
    public ExpenseCategoryGetDto getCategoryById(@PathVariable("id") Long id) {
        return modelMapper.map(categoryService.getCategoryById(id), ExpenseCategoryGetDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create expense category")
    public ExpenseCategoryGetDto createCategory(@RequestBody ExpenseCategoryCreateDto categoryCreateDto) {
        var category = modelMapper.map(categoryCreateDto, ExpenseCategory.class);
        return modelMapper.map(categoryService.addCategory(category), ExpenseCategoryGetDto.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete expense category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "Expense category is not found", content = @Content)})
    public void deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
    }
}
