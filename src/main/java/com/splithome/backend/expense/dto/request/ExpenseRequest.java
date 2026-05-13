package com.splithome.backend.expense.dto.request;

import java.util.UUID;

public record ExpenseRequest(UUID tenancyId,
                             String description,
                             Double amount){
}
