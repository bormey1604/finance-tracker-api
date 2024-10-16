package com.techgirl.finance_tracker_api.service;

import com.techgirl.finance_tracker_api.dto.BudgetDto;
import com.techgirl.finance_tracker_api.model.Budget;
import com.techgirl.finance_tracker_api.model.MyUser;
import com.techgirl.finance_tracker_api.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public BudgetDto createBudget(Budget budget) {
        Budget budgetSaved = budgetRepository.save(budget);

        BudgetDto budgetDto = new BudgetDto();
        budgetDto.setId(budgetSaved.getId());
        budgetDto.setBudgetLimit(budgetSaved.getBudgetLimit());
        budgetDto.setCategory(budgetSaved.getCategory());

        return budgetDto;
    }

    public List<BudgetDto> getUserBudgets(MyUser user) {
        List<Budget> budgets = budgetRepository.findByUser(user);
        List<BudgetDto> budgetDtos = new ArrayList<>();

        for (Budget budget : budgets) {
            BudgetDto budgetDto = new BudgetDto();
            budgetDto.setId(budget.getId());
            budgetDto.setBudgetLimit(budget.getBudgetLimit());
            budgetDto.setCategory(budget.getCategory());
            budgetDtos.add(budgetDto);
        }
        return budgetDtos;
    }

    public Budget updateBudget(Long id, Budget updatedBudget) {
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget not found!"));
        budget.setBudgetLimit(updatedBudget.getBudgetLimit());
        budget.setCategory(updatedBudget.getCategory());
        return budgetRepository.save(budget);
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
}
