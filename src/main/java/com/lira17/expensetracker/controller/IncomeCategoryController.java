package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.dto.create.IncomeCategoryCreateDto;
import com.lira17.expensetracker.dto.get.IncomeCategoryGetDto;
import com.lira17.expensetracker.mapper.IncomeCategoryMapper;
import com.lira17.expensetracker.service.IncomeCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Income Category", description = "Income Category API")
@RequestMapping(API + "income-categories")
public class IncomeCategoryController {

    @Autowired
    private IncomeCategoryService incomeCategoryService;

    @Autowired
    private IncomeCategoryMapper incomeCategoryMapper;


    @GetMapping
    @Operation(summary = "Get all income categories")
    public List<IncomeCategoryGetDto> getAllCategories() {
        return incomeCategoryMapper.convertToDto(incomeCategoryService.getAllCategories());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get income category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Income category is found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = IncomeCategoryGetDto.class))}),
            @ApiResponse(responseCode = "404", description = "Income category is not found", content = @Content)})
    public IncomeCategoryGetDto getCategoryById(@PathVariable("id") Long id) {
        return incomeCategoryMapper.convertToDto(incomeCategoryService.getCategoryById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an income category")
    public IncomeCategoryGetDto createCategory(@RequestBody IncomeCategoryCreateDto categoryCreateDto) {
        var incomeCategory = incomeCategoryMapper.convertToCategory(categoryCreateDto);
        return incomeCategoryMapper.convertToDto(incomeCategoryService.addCategory(incomeCategory));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "Income category is not found", content = @Content)})
    public void deleteCategory(@PathVariable("id") Long id) {
        incomeCategoryService.deleteCategory(id);
    }
}
