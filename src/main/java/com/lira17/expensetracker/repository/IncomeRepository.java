package com.lira17.expensetracker.repository;

import com.lira17.expensetracker.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByMonthAndYearOrderByDate(int month, int year);

    List<Income> findByYearOrderByDate(int year);

    @Query("SELECT SUM(i.amountInMainCurrency) FROM Income i")
    Optional<Double> getTotalIncomesAmount();
}
