package com.splithome.backend.expense.dto.response;

import java.util.UUID;

public record ExpenseSplitResponse(UUID id,
                                   UUID expenseId,
                                   String memberName,
                                   Double sharePercentage,
                                   boolean paid) {
}
