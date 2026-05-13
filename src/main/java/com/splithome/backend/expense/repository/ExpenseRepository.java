package com.splithome.backend.expense.repository;

import com.splithome.backend.expense.entity.Expense;
import com.splithome.backend.tenancy.entity.Tenancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    List<Expense> findAllByTenancy(Tenancy tenancy);

}
