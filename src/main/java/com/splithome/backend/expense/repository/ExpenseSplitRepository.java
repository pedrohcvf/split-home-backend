package com.splithome.backend.expense.repository;

import com.splithome.backend.expense.entity.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, UUID> {
}
