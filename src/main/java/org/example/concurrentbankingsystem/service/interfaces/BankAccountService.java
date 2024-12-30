package org.example.concurrentbankingsystem.service.interfaces;

import org.example.concurrentbankingsystem.dto.CreateBankAccountDTO;
import org.example.concurrentbankingsystem.dto.DepositDTO;
import org.example.concurrentbankingsystem.dto.TransferDTO;
import org.example.concurrentbankingsystem.dto.WithdrawDTO;
import org.example.concurrentbankingsystem.model.BankAccount;

import java.util.List;

public interface BankAccountService {

    List<BankAccount> getAllBankAccounts();

    BankAccount getBankAccountById(Long id);

    BankAccount createBankAccount(CreateBankAccountDTO createBankAccountDTO);

    BankAccount updateBankAccount(Long bankAccountId, CreateBankAccountDTO createBankAccountDTO);

    void deleteBankAccountById(Long id);

    BankAccount getBankAccountByAccountNumber(String accountNumber);

    BankAccount deposit(DepositDTO depositDTO);

    BankAccount withdraw(WithdrawDTO withdrawDTO);

    List<BankAccount> transfer(TransferDTO transferDTO);
}
