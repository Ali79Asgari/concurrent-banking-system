package org.example.concurrentbankingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String transactionType;
    private double amount;
    private String accountNumber;
    private LocalDateTime transactionDate;

    public Transaction(String transactionType, double amount, String accountNumber) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.transactionDate = LocalDateTime.now();
    }
}
