package com.lira17.expensetracker.service;

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
        return incomeCategoryRepository.findById(id).orElseThrow();
    }

    @Transactional
    public IncomeCategory addCategory(IncomeCategory category) {
        return incomeCategoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(long id) {
        var category = getCategoryById(id);
        incomeCategoryRepository.delete(category);
    }
}
