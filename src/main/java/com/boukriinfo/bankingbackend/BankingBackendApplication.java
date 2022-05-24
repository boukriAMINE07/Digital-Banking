package com.boukriinfo.bankingbackend;

import com.boukriinfo.bankingbackend.dtos.BankAccountDTO;
import com.boukriinfo.bankingbackend.dtos.CurrentBankAccountDTO;
import com.boukriinfo.bankingbackend.dtos.CustomerDTO;
import com.boukriinfo.bankingbackend.dtos.SavingBankAccountDTO;
import com.boukriinfo.bankingbackend.entities.*;
import com.boukriinfo.bankingbackend.enums.AccountStatus;
import com.boukriinfo.bankingbackend.enums.OperationType;
import com.boukriinfo.bankingbackend.exceptions.BalanceNotSufficientException;
import com.boukriinfo.bankingbackend.exceptions.BankAccountNotFoundException;
import com.boukriinfo.bankingbackend.exceptions.CustomerNotFoundException;
import com.boukriinfo.bankingbackend.repositories.AccountOperationRepository;
import com.boukriinfo.bankingbackend.repositories.BankAccountRepository;
import com.boukriinfo.bankingbackend.repositories.CustomerRepository;
import com.boukriinfo.bankingbackend.services.BankAccountService;
import com.boukriinfo.bankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Salah","Mohssin","Malak").forEach(name->{
                CustomerDTO customerDTO=new CustomerDTO();
                customerDTO.setName(name);
                customerDTO.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customerDTO);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                     bankAccountService.saveCurrentBankAccount(Math.random()*900000, 9000, customer.getId());
                     bankAccountService.saveSavingBankAccount(Math.random() * 900000, 5.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }

            }
            );
            List<BankAccountDTO> bankAccounts = bankAccountService.listBankAccounts();
            for (BankAccountDTO bankAccount:bankAccounts) {
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if (bankAccount instanceof SavingBankAccountDTO){
                        accountId= ((SavingBankAccountDTO) bankAccount).getId();
                    }else{
                        accountId=((CurrentBankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId,10000+Math.random()*12000,"Credit");
                    bankAccountService.debit(accountId,1000+Math.random()*1200,"Debit");
                }
            }
        };
    }


    // @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            AccountOperationRepository accountOperationRepository,
                            BankAccountRepository bankAccountRepository){
        return args -> {
            Stream.of("Amine","Jamal","Naima").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*1000000);
                currentAccount.setCustomer(cust);
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCreateDate(new Date());
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount=new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setBalance(Math.random()*1000000);
                savingAccount.setCreateDate(new Date());
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setBankAccount(acc);
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setOperationDate(new Date());
                    accountOperationRepository.save(accountOperation);
                }
            });



        };
    }


}
