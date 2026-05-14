package com.splithome.backend.expense.controller;

import com.splithome.backend.expense.dto.request.ExpenseRequest;
import com.splithome.backend.expense.dto.response.ExpenseResponse;
import com.splithome.backend.expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    // CRIAR DESPESA
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request){
        ExpenseResponse expense = expenseService.createExpense(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(expense);
    }

    // LISTAR DESPESAS
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> listExpenses(@RequestParam UUID id){
        List<ExpenseResponse> expenses = expenseService.listExpenses(id);
        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }
}
