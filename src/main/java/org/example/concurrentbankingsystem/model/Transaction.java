package org.example.concurrentbankingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.concurrentbankingsystem.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id")
    private BankAccount sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_account_id")
    private BankAccount targetAccount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String description;

    @Column(nullable = false)
    private boolean successful;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public static Transaction createDeposit(BankAccount targetAccount, BigDecimal amount) {
        return Transaction.builder()
                .targetAccount(targetAccount)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .successful(true)
                .description("Deposit to account: " + targetAccount.getAccountNumber())
                .build();
    }

    public static Transaction createWithdrawal(BankAccount sourceAccount, BigDecimal amount) {
        return Transaction.builder()
                .sourceAccount(sourceAccount)
                .type(TransactionType.WITHDRAWAL)
                .amount(amount)
                .successful(true)
                .description("Withdrawal from account: " + sourceAccount.getAccountNumber())
                .build();
    }

    public static Transaction createTransfer(BankAccount sourceAccount, BankAccount targetAccount, BigDecimal amount) {
        return Transaction.builder()
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .successful(true)
                .description("Transfer from account: " + sourceAccount.getAccountNumber() +
                        " to account: " + targetAccount.getAccountNumber())
                .build();
    }
}
