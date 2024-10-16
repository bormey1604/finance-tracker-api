package com.techgirl.finance_tracker_api.dto;


import lombok.Data;

@Data
public class BudgetDto {

    private Long id;
    private Double budgetLimit;
    private String category;
}
