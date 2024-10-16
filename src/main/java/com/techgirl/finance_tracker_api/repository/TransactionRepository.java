package com.techgirl.finance_tracker_api.repository;

import com.techgirl.finance_tracker_api.model.Transaction;
import com.techgirl.finance_tracker_api.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByUserId(Long id);
    List<Transaction> findByType(TransactionType type);
}
