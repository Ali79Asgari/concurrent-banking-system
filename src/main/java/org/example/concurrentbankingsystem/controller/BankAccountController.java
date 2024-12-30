package org.example.concurrentbankingsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.concurrentbankingsystem.dto.CreateBankAccountDTO;
import org.example.concurrentbankingsystem.dto.DepositDTO;
import org.example.concurrentbankingsystem.dto.TransferDTO;
import org.example.concurrentbankingsystem.dto.WithdrawDTO;
import org.example.concurrentbankingsystem.model.BankAccount;
import org.example.concurrentbankingsystem.service.interfaces.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping("/{bankAccountId}")
    public ResponseEntity<BankAccount> getBankAccountById(@PathVariable Long bankAccountId) {
        BankAccount bankAccount = bankAccountService.getBankAccountById(bankAccountId);
        return ResponseEntity.ok(bankAccount);
    }

    @GetMapping("/")
    public ResponseEntity<List<BankAccount>> getBankAccounts() {
        List<BankAccount> bankAccountList = bankAccountService.getAllBankAccounts();
        return ResponseEntity.ok(bankAccountList);
    }

    @PostMapping("/")
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody CreateBankAccountDTO bankAccountDTO) {
        BankAccount createdBankAccount = bankAccountService.createBankAccount(bankAccountDTO);
        return ResponseEntity.ok(createdBankAccount);
    }

    @PutMapping("/{bankAccountId}")
    public ResponseEntity<BankAccount> updateBankAccount(@PathVariable Long bankAccountId, @RequestBody CreateBankAccountDTO bankAccountDTO) {
        BankAccount updatedBankAccount = bankAccountService.updateBankAccount(bankAccountId, bankAccountDTO);
        return ResponseEntity.ok(updatedBankAccount);
    }

    @DeleteMapping("/{bankAccountId}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long bankAccountId) {
        bankAccountService.deleteBankAccountById(bankAccountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/accountNumber")
    public ResponseEntity<BankAccount> getBankAccountByAccountNumber(@RequestParam String accountNumber) {
        BankAccount bankAccount = bankAccountService.getBankAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/deposit")
    public ResponseEntity<BankAccount> deposit(@RequestBody DepositDTO depositDTO) {
        BankAccount bankAccount = bankAccountService.deposit(depositDTO);
        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BankAccount> withdraw(@RequestBody WithdrawDTO withdrawDTO) {
        BankAccount bankAccount = bankAccountService.withdraw(withdrawDTO);
        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/transfer")
    public ResponseEntity<List<BankAccount>> transfer(@RequestBody TransferDTO transferDTO) {
        List<BankAccount> bankAccounts = bankAccountService.transfer(transferDTO);
        return ResponseEntity.ok(bankAccounts);
    }
}
