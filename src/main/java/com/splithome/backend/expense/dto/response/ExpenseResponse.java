package com.splithome.backend.expense.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponse(UUID tenancyId,
                              UUID expenseId,
                              String description,
                              Double amount,
                              String paidByName,
                              LocalDateTime createdAt) {
}
