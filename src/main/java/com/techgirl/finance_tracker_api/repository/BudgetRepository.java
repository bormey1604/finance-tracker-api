package com.techgirl.finance_tracker_api.repository;

import com.techgirl.finance_tracker_api.model.Budget;
import com.techgirl.finance_tracker_api.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser(MyUser user);
}
