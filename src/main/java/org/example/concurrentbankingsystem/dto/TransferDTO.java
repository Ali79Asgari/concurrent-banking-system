package org.example.concurrentbankingsystem.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferDTO {
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private BigDecimal amount;
}
