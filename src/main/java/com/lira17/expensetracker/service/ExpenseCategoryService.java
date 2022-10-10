package com.lira17.expensetracker.service;

import com.lira17.expensetracker.exception.NotFoundException;
import com.lira17.expensetracker.model.ExpenseCategory;
import com.lira17.expensetracker.repository.ExpenseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpenseCategoryService {

    @Autowired
    ExpenseCategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ExpenseCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ExpenseCategory getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(ExpenseCategory.class));
    }

    @Transactional
    public ExpenseCategory addCategory(ExpenseCategory category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(long id) {
        var category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}
