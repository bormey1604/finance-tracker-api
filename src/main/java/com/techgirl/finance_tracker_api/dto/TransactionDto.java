package com.techgirl.finance_tracker_api.dto;

import com.techgirl.finance_tracker_api.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionDto {

    private String id;
    private String category;
    private TransactionType type;
    private double amount;
    private String description;
    private Date date;
}
