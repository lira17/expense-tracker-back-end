package com.lira17.expensetracker.service;

import com.lira17.expensetracker.exception.NotFoundException;
import com.lira17.expensetracker.model.IncomeCategory;
import com.lira17.expensetracker.repository.IncomeCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IncomeCategoryService {

    @Autowired
    IncomeCategoryRepository incomeCategoryRepository;

    @Transactional(readOnly = true)
    public List<IncomeCategory> getAllCategories() {
        return incomeCategoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public IncomeCategory getCategoryById(long id) {
        return incomeCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException(IncomeCategory.class));
    }

    @Transactional
    public IncomeCategory addCategory(IncomeCategory category) {
        return incomeCategoryRepository.save(category);
    }

    @Transactional
    public IncomeCategory updateCategory(long id, IncomeCategory newCategory) {
        var oldCategory = getCategoryById(id);
        return incomeCategoryRepository.save(updateOldCategory(newCategory, oldCategory));
    }

    @Transactional
    public void deleteCategory(long id) {
        var category = getCategoryById(id);
        incomeCategoryRepository.delete(category);
    }

    private IncomeCategory updateOldCategory(IncomeCategory newCategory, IncomeCategory oldCategory) {
        oldCategory.setTitle(newCategory.getTitle());
        oldCategory.setType(newCategory.getType());
        return oldCategory;
    }
}
