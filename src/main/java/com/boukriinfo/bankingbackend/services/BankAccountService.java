package com.boukriinfo.bankingbackend.services;

import com.boukriinfo.bankingbackend.dtos.*;
import com.boukriinfo.bankingbackend.entities.BankAccount;
import com.boukriinfo.bankingbackend.entities.CurrentAccount;
import com.boukriinfo.bankingbackend.entities.Customer;
import com.boukriinfo.bankingbackend.entities.SavingAccount;
import com.boukriinfo.bankingbackend.exceptions.BalanceNotSufficientException;
import com.boukriinfo.bankingbackend.exceptions.BankAccountNotFoundException;
import com.boukriinfo.bankingbackend.exceptions.CustomerNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BankAccountService {
     CustomerDTO saveCustomer(CustomerDTO customerDTO);
     List<CustomerDTO> listCustomers();
     List<BankAccountDTO> listBankAccounts();
     CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
     SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

     CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

     BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
     void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
     void credit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
     void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

     CustomerDTO updateCustomer(CustomerDTO customerDTO);

     void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

     AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

     List<CustomerDTO> searchCustomers(String keyword);
}
