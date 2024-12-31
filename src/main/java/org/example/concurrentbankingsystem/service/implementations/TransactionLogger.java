package org.example.concurrentbankingsystem.service.implementations;

import lombok.RequiredArgsConstructor;
import org.example.concurrentbankingsystem.model.BankAccount;
import org.example.concurrentbankingsystem.model.Transaction;
import org.example.concurrentbankingsystem.model.enums.TransactionType;
import org.example.concurrentbankingsystem.repository.BankAccountRepository;
import org.example.concurrentbankingsystem.repository.TransactionRepository;
import org.example.concurrentbankingsystem.service.interfaces.TransactionObserver;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionLogger implements TransactionObserver {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    private static final String LOG_FILE = "transactions.log";

    @Override
    public void onTransaction(String sourceAccountNumber, String targetAccountNumber, String transactionType, BigDecimal amount) {

        Optional<BankAccount> sourceBankAccount = Optional.empty();
        Optional<BankAccount> targetBankAccount = Optional.empty();

        if (!sourceAccountNumber.equals(targetAccountNumber)) {
            sourceBankAccount = bankAccountRepository.findByAccountNumber(sourceAccountNumber);
            targetBankAccount = bankAccountRepository.findByAccountNumber(targetAccountNumber);
        } else {
            sourceBankAccount = bankAccountRepository.findByAccountNumber(sourceAccountNumber);
            targetBankAccount = sourceBankAccount;
        }

        if (sourceBankAccount.isPresent() && targetBankAccount.isPresent()) {
            Transaction transaction = Transaction.builder()
                    .sourceAccount(sourceBankAccount.get())
                    .targetAccount(targetBankAccount.get())
                    .type(TransactionType.valueOf(transactionType))
                    .amount(amount)
                    .build();

            transactionRepository.save(transaction);

            String logMessage = String.format("SourceAccount: %s, TargetAccount: %s, Type: %s, Amount: %.2f\n", sourceAccountNumber, targetAccountNumber, transactionType, amount);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                writer.write(logMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }
    }
}
