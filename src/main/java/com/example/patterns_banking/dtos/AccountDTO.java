package com.example.patterns_banking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;
    private String number;
    private String type;
    private BigDecimal balance;
    private Boolean isActive = true;
}
