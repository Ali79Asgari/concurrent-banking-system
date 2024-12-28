package org.example.concurrentbankingsystem.repository;

import org.example.concurrentbankingsystem.model.BankAccount;
import org.example.concurrentbankingsystem.model.Transaction;
import org.example.concurrentbankingsystem.model.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findBySourceAccount(BankAccount sourceAccount);

    List<Transaction> findByTargetAccount(BankAccount targetAccount);

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount = :account OR t.targetAccount = :account")
    List<Transaction> findAllByBankAccount(@Param("account") BankAccount account);

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount = :account OR t.targetAccount = :account")
    Page<Transaction> findAllByBankAccount(@Param("account") BankAccount account, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.sourceAccount = :account OR t.targetAccount = :account) " +
            "AND t.timestamp BETWEEN :startDate AND :endDate")
    List<Transaction> findByBankAccountAndDateRange(
            @Param("account") BankAccount account,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<Transaction> findByType(TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.timestamp >= :startDate")
    List<Transaction> findTransactionsAfterDate(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE (t.sourceAccount = :account OR t.targetAccount = :account) " +
            "AND t.successful = true")
    long countSuccessfulTransactions(@Param("account") BankAccount account);

    @Query("SELECT t FROM Transaction t WHERE t.successful = false")
    List<Transaction> findFailedTransactions();
}
