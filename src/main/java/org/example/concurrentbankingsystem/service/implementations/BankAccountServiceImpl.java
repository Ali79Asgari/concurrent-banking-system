package org.example.concurrentbankingsystem.service.implementations;

import lombok.RequiredArgsConstructor;
import org.example.concurrentbankingsystem.dto.CreateBankAccountDTO;
import org.example.concurrentbankingsystem.dto.DepositDTO;
import org.example.concurrentbankingsystem.dto.TransferDTO;
import org.example.concurrentbankingsystem.dto.WithdrawDTO;
import org.example.concurrentbankingsystem.model.BankAccount;
import org.example.concurrentbankingsystem.model.enums.TransactionType;
import org.example.concurrentbankingsystem.repository.BankAccountRepository;
import org.example.concurrentbankingsystem.service.interfaces.BankAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionLogger transactionLogger;

    @Override
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount getBankAccountById(Long id) {
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
        if (bankAccount.isPresent()) {
            return bankAccount.get();
        } else {
            throw new IllegalArgumentException("Bank account not found");
        }
    }

    @Override
    public BankAccount createBankAccount(CreateBankAccountDTO createBankAccountDTO) {
        BankAccount bankAccount = BankAccount.builder()
                .accountHolderName(createBankAccountDTO.getAccountHolderName())
                .balance(createBankAccountDTO.getBalance())
                .build();

        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount updateBankAccount(Long bankAccountId, CreateBankAccountDTO createBankAccountDTO) {
        return bankAccountRepository.findById(bankAccountId).map(bankAccount1 -> {
            bankAccount1.setAccountHolderName(createBankAccountDTO.getAccountHolderName());
            bankAccount1.setBalance(createBankAccountDTO.getBalance());
            return bankAccountRepository.save(bankAccount1);
        }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void deleteBankAccountById(Long id) {
        bankAccountRepository.deleteById(id);
    }

    @Override
    public BankAccount getBankAccountByAccountNumber(String accountNumber) {
        Optional<BankAccount> bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);
        if (bankAccount.isPresent()) {
            return bankAccount.get();
        } else {
            throw new IllegalArgumentException("Bank account not found");
        }
    }

    @Override
    public BankAccount deposit(DepositDTO depositDTO) {
        if (depositDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Optional<BankAccount> bankAccount = bankAccountRepository.findByAccountNumber(depositDTO.getSourceAccountNumber());
        if (bankAccount.isPresent()) {
            bankAccount.get().setBalance(bankAccount.get().getBalance().add(depositDTO.getAmount()));
            bankAccountRepository.save(bankAccount.get());

            transactionLogger.onTransaction(depositDTO.getSourceAccountNumber(), depositDTO.getSourceAccountNumber(), TransactionType.DEPOSIT.name(), depositDTO.getAmount());

            return bankAccount.get();
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public BankAccount withdraw(WithdrawDTO withdrawDTO) {
        if (withdrawDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Optional<BankAccount> bankAccount = bankAccountRepository.findByAccountNumber(withdrawDTO.getSourceAccountNumber());
        if (bankAccount.isPresent()) {
            bankAccount.get().setBalance(bankAccount.get().getBalance().subtract(withdrawDTO.getAmount()));
            bankAccountRepository.save(bankAccount.get());

            transactionLogger.onTransaction(withdrawDTO.getSourceAccountNumber(), withdrawDTO.getSourceAccountNumber(), TransactionType.WITHDRAWAL.name(), withdrawDTO.getAmount());

            return bankAccount.get();
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public List<BankAccount> transfer(TransferDTO transferDTO) {
        if (transferDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Optional<BankAccount> sourceBankAccount = bankAccountRepository.findByAccountNumber(transferDTO.getSourceAccountNumber());
        Optional<BankAccount> targetBankAccount = bankAccountRepository.findByAccountNumber(transferDTO.getTargetAccountNumber());

        if (sourceBankAccount.isPresent() && targetBankAccount.isPresent()) {
            if (sourceBankAccount.get().getBalance().compareTo(transferDTO.getAmount()) >= 0) {
                sourceBankAccount.get().setBalance(sourceBankAccount.get().getBalance().subtract(transferDTO.getAmount()));
                targetBankAccount.get().setBalance(targetBankAccount.get().getBalance().add(transferDTO.getAmount()));

                bankAccountRepository.save(sourceBankAccount.get());
                bankAccountRepository.save(targetBankAccount.get());

                transactionLogger.onTransaction(transferDTO.getSourceAccountNumber(), transferDTO.getTargetAccountNumber(), TransactionType.TRANSFER.name(), transferDTO.getAmount());

                return List.of(sourceBankAccount.get(), targetBankAccount.get());
            } else {
                throw new IllegalArgumentException("Amount is greater than SourceBankAccount's balance!");
            }
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }
}
