package org.example.concurrentbankingsystem;

import org.example.concurrentbankingsystem.dto.CreateBankAccountDTO;
import org.example.concurrentbankingsystem.dto.DepositDTO;
import org.example.concurrentbankingsystem.dto.TransferDTO;
import org.example.concurrentbankingsystem.dto.WithdrawDTO;
import org.example.concurrentbankingsystem.model.BankAccount;
import org.example.concurrentbankingsystem.service.interfaces.BankAccountService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.Scanner;

@SpringBootApplication
public class ConcurrentBankingSystemApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ConcurrentBankingSystemApplication.class, args);

        BankAccountService bankAccountService = context.getBean(BankAccountService.class);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Banking System Menu ===");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. View Account");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1:
                        createAccount(bankAccountService, scanner);
                        break;
                    case 2:
                        deposit(bankAccountService, scanner);
                        break;
                    case 3:
                        withdraw(bankAccountService, scanner);
                        break;
                    case 4:
                        transfer(bankAccountService, scanner);
                        break;
                    case 5:
                        viewAccount(bankAccountService, scanner);
                        break;
                    case 6:
                        System.out.println("Exiting... Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createAccount(BankAccountService bankAccountService, Scanner scanner) {
        System.out.print("Enter account holder name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        BigDecimal balance = scanner.nextBigDecimal();

        CreateBankAccountDTO createBankAccountDTO = CreateBankAccountDTO.builder()
                .accountHolderName(name)
                .balance(balance)
                .build();

        BankAccount account = bankAccountService.createBankAccount(createBankAccountDTO);
        System.out.println("Account created successfully with account number: " + account.getAccountNumber());
    }

    private static void deposit(BankAccountService bankAccountService, Scanner scanner) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();
        System.out.print("Enter deposit amount: ");
        BigDecimal amount = scanner.nextBigDecimal();

        DepositDTO depositDTO = DepositDTO.builder()
                .sourceAccountNumber(accountNumber)
                .amount(amount)
                .build();

        BankAccount account = bankAccountService.deposit(depositDTO);
        System.out.println("Deposit successful. New balance: " + account.getBalance());
    }

    private static void withdraw(BankAccountService bankAccountService, Scanner scanner) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();
        System.out.print("Enter withdrawal amount: ");
        BigDecimal amount = scanner.nextBigDecimal();

        WithdrawDTO withdrawDTO = WithdrawDTO.builder()
                .sourceAccountNumber(accountNumber)
                .amount(amount)
                .build();

        BankAccount account = bankAccountService.withdraw(withdrawDTO);
        System.out.println("Withdrawal successful. New balance: " + account.getBalance());
    }

    private static void transfer(BankAccountService bankAccountService, Scanner scanner) {
        System.out.print("Enter source account number: ");
        String sourceAccountNumber = scanner.next();
        System.out.print("Enter destination account number: ");
        String targetAccountNumber = scanner.next();
        System.out.print("Enter transfer amount: ");
        BigDecimal amount = scanner.nextBigDecimal();

        TransferDTO transferDTO = TransferDTO.builder()
                .sourceAccountNumber(sourceAccountNumber)
                .targetAccountNumber(targetAccountNumber)
                .amount(amount)
                .build();

        bankAccountService.transfer(transferDTO);
        System.out.println("Transfer successful.");
    }

    private static void viewAccount(BankAccountService bankAccountService, Scanner scanner) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();

        BankAccount account = bankAccountService.getBankAccountByAccountNumber(accountNumber);
        System.out.println("Account Details:");
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Account Holder: " + account.getAccountHolderName());
        System.out.println("Balance: " + account.getBalance());
    }

}
