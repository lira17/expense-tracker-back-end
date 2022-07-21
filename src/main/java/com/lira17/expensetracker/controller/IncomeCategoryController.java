package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.dto.create.IncomeCategoryCreateDto;
import com.lira17.expensetracker.dto.get.IncomeCategoryGetDto;
import com.lira17.expensetracker.model.IncomeCategory;
import com.lira17.expensetracker.service.IncomeCategoryService;
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
@RequestMapping(API + "income-categories")
public class IncomeCategoryController {

    @Autowired
    private IncomeCategoryService incomeCategoryService;

    @Autowired
    private ModelMapper modelMapper;

    private static final Type LIST_OF_CATEGORIES_TYPE = new TypeToken<List<IncomeCategoryGetDto>>() {
    }.getType();

    @GetMapping
    public List<IncomeCategoryGetDto> getAllCategories() {
        return modelMapper.map(incomeCategoryService.getAllCategories(), LIST_OF_CATEGORIES_TYPE);
    }

    @GetMapping(value = "/{id}")
    public IncomeCategoryGetDto getCategoryById(@PathVariable("id") Long id) {
        return modelMapper.map(incomeCategoryService.getCategoryById(id), IncomeCategoryGetDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncomeCategoryGetDto createCategory(@RequestBody IncomeCategoryCreateDto categoryCreateDto) {
        var category = modelMapper.map(categoryCreateDto, IncomeCategory.class);
        return modelMapper.map(incomeCategoryService.addCategory(category), IncomeCategoryGetDto.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable("id") Long id) {
        incomeCategoryService.deleteCategory(id);
    }
}
