package org.example.concurrentbankingsystem.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateBankAccountDTO {
    private String accountHolderName;
    private BigDecimal balance;
}
