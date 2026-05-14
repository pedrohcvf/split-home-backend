package com.splithome.backend.expense.service;

import com.splithome.backend.exception.customs.TenancyInactiveException;
import com.splithome.backend.exception.customs.TenancyNotFoundException;
import com.splithome.backend.exception.customs.UserNotMemberException;
import com.splithome.backend.expense.dto.request.ExpenseRequest;
import com.splithome.backend.expense.dto.response.ExpenseResponse;
import com.splithome.backend.expense.entity.Expense;
import com.splithome.backend.expense.entity.ExpenseSplit;
import com.splithome.backend.expense.repository.ExpenseRepository;
import com.splithome.backend.expense.repository.ExpenseSplitRepository;
import com.splithome.backend.tenancy.entity.Tenancy;
import com.splithome.backend.tenancy.entity.TenancyMember;
import com.splithome.backend.tenancy.repository.TenancyMemberRepository;
import com.splithome.backend.tenancy.repository.TenancyRepository;
import com.splithome.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final TenancyRepository tenancyRepository;

    private final ExpenseRepository expenseRepository;

    private final ExpenseSplitRepository expenseSplitRepository;

    private final TenancyMemberRepository tenancyMemberRepository;

    // CRIAR DESPESA
    public ExpenseResponse createExpense(ExpenseRequest request){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Tenancy tenancy = tenancyRepository.findById(request.tenancyId())
                .orElseThrow(()-> new TenancyNotFoundException());

        if (!tenancy.isActive()){
            throw new TenancyInactiveException(tenancy.toString());
        }

        if (!tenancyMemberRepository.existsByTenancyAndUser(tenancy, currentUser)){
            throw new UserNotMemberException(currentUser.getEmail());
        }

        Expense expense = Expense.builder()
                .tenancy(tenancy)
                .paidBy(currentUser)
                .description(request.description())
                .amount(request.amount())
                .build();

        Expense expenseSaved = expenseRepository.save(expense);

        List<TenancyMember> members = tenancyMemberRepository.findAllByTenancy(tenancy);

        for (TenancyMember member: members){
            ExpenseSplit expenseSplit = ExpenseSplit.builder()
                    .expense(expense)
                    .member(member)
                    .paid(false)
                    .build();

            expenseSplitRepository.save(expenseSplit);
        }

        return new ExpenseResponse(
                expenseSaved.getTenancy().getId(),
                expenseSaved.getId(),
                expenseSaved.getDescription(),
                expenseSaved.getAmount(),
                expenseSaved.getPaidBy().getName(),
                expenseSaved.getCreatedAt());
    }

    // LISTAR DESPESAS
    public List<ExpenseResponse> listExpenses(UUID tenancyId){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Tenancy tenancy = tenancyRepository.findById(tenancyId)
                .orElseThrow(() -> new TenancyNotFoundException());

        if (!tenancyMemberRepository.existsByTenancyAndUser(tenancy, currentUser)){
            throw new UserNotMemberException(currentUser.getEmail());
        }

        return expenseRepository.findAllByTenancy(tenancy)
                .stream()
                .map(expense -> new ExpenseResponse(
                        expense.getTenancy().getId(),
                        expense.getId(),
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getPaidBy().getName(),
                        expense.getCreatedAt()))
                .toList();
    }
}
