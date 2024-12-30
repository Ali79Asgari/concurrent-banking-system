package org.example.concurrentbankingsystem.repository;

import jakarta.persistence.LockModeType;
import org.example.concurrentbankingsystem.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM BankAccount a WHERE a.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumberWithLock(@Param("accountNumber") String accountNumber);

    Optional<BankAccount> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT a FROM BankAccount a WHERE a.balance > :minimumBalance")
    List<BankAccount> findAccountsWithBalanceGreaterThan(@Param("minimumBalance") BigDecimal minimumBalance);

    @Query("SELECT a FROM BankAccount a WHERE a.accountHolderName LIKE %:name%")
    List<BankAccount> findByAccountHolderNameContaining(@Param("name") String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM BankAccount a WHERE a.id = :id")
    Optional<BankAccount> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT COUNT(a) > 0 FROM BankAccount a WHERE a.accountNumber = :accountNumber AND a.balance >= :amount")
    boolean hasEnoughBalance(@Param("accountNumber") String accountNumber, @Param("amount") BigDecimal amount);
}
