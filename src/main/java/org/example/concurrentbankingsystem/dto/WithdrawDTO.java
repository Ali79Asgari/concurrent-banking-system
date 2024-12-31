package org.example.concurrentbankingsystem.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WithdrawDTO {
    private String sourceAccountNumber;
    private BigDecimal amount;
}
