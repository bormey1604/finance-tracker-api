package com.techgirl.finance_tracker_api.controller;

import com.techgirl.finance_tracker_api.dto.BudgetDto;
import com.techgirl.finance_tracker_api.model.Budget;
import com.techgirl.finance_tracker_api.model.MyUser;
import com.techgirl.finance_tracker_api.model.response.ApiResponse;
import com.techgirl.finance_tracker_api.service.BudgetService;
import com.techgirl.finance_tracker_api.utility.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final AuthUtil authUtil;

    public BudgetController(BudgetService budgetService, AuthUtil authUtil) {
        this.budgetService = budgetService;
        this.authUtil = authUtil;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createBudget(@RequestBody Budget budget, HttpServletRequest request) {
        MyUser user = authUtil.getAuthenticatedUser(request);
        if (user != null) {
            budget.setUser(user);
            budgetService.createBudget(budget);
            return ResponseEntity.ok(new ApiResponse("200", "Budget successfully created!"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("401", "Unauthorized: User not found!"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getUserBudgets(HttpServletRequest request) {
        MyUser user = authUtil.getAuthenticatedUser(request);

        if (user != null) {

            List<BudgetDto> budgets = budgetService.getUserBudgets(user);
            Map<String, Object> map = Map.of("budgets", budgets);

            return ResponseEntity.ok(new ApiResponse("200", "Budget successfully created!",map));

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBudget(@PathVariable Long id, @RequestBody Budget budget, HttpServletRequest request) {
        MyUser user = authUtil.getAuthenticatedUser(request);
        if (user != null) {
            budgetService.updateBudget(id, budget);
            return ResponseEntity.ok(new ApiResponse("200", "Budget successfully updated!"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("401", "Unauthorized: User not found!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBudget(@PathVariable Long id, HttpServletRequest request) {
        MyUser user = authUtil.getAuthenticatedUser(request);
        if (user != null) {
            budgetService.deleteBudget(id);
            return ResponseEntity.ok(new ApiResponse("200", "Budget successfully deleted!"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("401", "Unauthorized: User not found!"));
    }
}
