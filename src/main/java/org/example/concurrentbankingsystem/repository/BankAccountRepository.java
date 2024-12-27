package org.example.concurrentbankingsystem.repository;

import org.example.concurrentbankingsystem.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

    BankAccount findByAccountNumber(String accountNumber);
}
