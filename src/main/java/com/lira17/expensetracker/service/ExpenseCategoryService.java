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
    ExpenseCategoryRepository expenseCategoryRepository;

    @Transactional(readOnly = true)
    public List<ExpenseCategory> getAllCategories() {
        return expenseCategoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ExpenseCategory getCategoryById(long id) {
        return expenseCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException(ExpenseCategory.class));
    }

    @Transactional
    public ExpenseCategory addCategory(ExpenseCategory category) {
        return expenseCategoryRepository.save(category);
    }


    @Transactional
    public ExpenseCategory updateCategory(long id, ExpenseCategory newCategory) {
        var oldCategory = getCategoryById(id);
        return expenseCategoryRepository.save(updateOldCategory(newCategory, oldCategory));
    }

    @Transactional
    public void deleteCategory(long id) {
        var category = getCategoryById(id);
        expenseCategoryRepository.delete(category);
    }

    private ExpenseCategory updateOldCategory(ExpenseCategory newCategory, ExpenseCategory oldCategory) {
        oldCategory.setTitle(newCategory.getTitle());
        oldCategory.setType(newCategory.getType());
        return oldCategory;
    }
}
