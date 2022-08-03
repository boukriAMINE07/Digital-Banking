package com.boukriinfo.bankingbackend.services;

import com.boukriinfo.bankingbackend.dtos.*;
import com.boukriinfo.bankingbackend.entities.*;
import com.boukriinfo.bankingbackend.enums.AccountStatus;
import com.boukriinfo.bankingbackend.enums.OperationType;
import com.boukriinfo.bankingbackend.exceptions.BalanceNotSufficientException;
import com.boukriinfo.bankingbackend.exceptions.BankAccountNotFoundException;
import com.boukriinfo.bankingbackend.exceptions.CustomerNotFoundException;
import com.boukriinfo.bankingbackend.mappers.BankAccountMapperImpl;
import com.boukriinfo.bankingbackend.repositories.AccountOperationRepository;
import com.boukriinfo.bankingbackend.repositories.BankAccountRepository;
import com.boukriinfo.bankingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository  accountOperationRepository;
    private BankAccountMapperImpl bankAccountMapper;
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Save new Customer");
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).collect(Collectors.toList());

        return customerDTOS;
    }

    @Override
    public List<BankAccountDTO> listBankAccounts() {
        List<BankAccount> bankAccounts=bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if(bankAccount instanceof SavingAccount){
                return bankAccountMapper.fromSavingBankAccount((SavingAccount) bankAccount);
            }else{
                CurrentAccount currentAccount=(CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentBankAccount(currentAccount);
            }


        }).collect(Collectors.toList());
        return bankAccountDTOS;

    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        CurrentAccount bankAccount=new CurrentAccount();

        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreateDate(new Date());
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setBalance(initialBalance);
        bankAccount.setOverDraft(overDraft);

        if(customer!=null){
            bankAccount.setCustomer(customer);
        }else {
            throw new CustomerNotFoundException("Customer not Found ");
        }

        CurrentAccount saveCurrentAccount = bankAccountRepository.save( bankAccount);

        return bankAccountMapper.fromCurrentBankAccount(saveCurrentAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        SavingAccount bankAccount=new SavingAccount();

        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreateDate(new Date());
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setBalance(initialBalance);
        bankAccount.setInterestRate(interestRate);

        if(customer!=null){
            bankAccount.setCustomer(customer);
        }else {
            throw new CustomerNotFoundException("Customer not Found ");
        }

        SavingAccount saveSavingAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.fromSavingBankAccount(saveSavingAccount);
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).
                orElseThrow(()->new CustomerNotFoundException("Customer Not found") );
        CustomerDTO customerDTO = bankAccountMapper.fromCustomer(customer);
        return customerDTO;
    }


    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account not Found "));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount=(SavingAccount) bankAccount;
            return bankAccountMapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
            return bankAccountMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account not Found "));

            if(bankAccount.getBalance()<amount)
                throw new BalanceNotSufficientException("Balance Not Sufficient");

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(amount);
        accountOperation.setDescription("debit");
        accountOperation.setType(OperationType.DEBIT);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account not Found "));
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance Not Sufficient");

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(amount);
        accountOperation.setDescription("credit");
        accountOperation.setType(OperationType.CREDIT);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {

                    debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
                    credit(accountIdDestination,amount,"Transfer from "+accountIdSource);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Save new Customer");
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId ){
        customerRepository.deleteById(customerId);
    }


    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperationDTO> accountOperationDTOS = accountOperationRepository.findByBankAccountId(accountId).
                stream().map(accountOperation ->
                        bankAccountMapper.fromAccountOperation(accountOperation)).
                collect(Collectors.toList());


        return accountOperationDTOS;
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount==null) throw new BankAccountNotFoundException("Account Not Found !!");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO     accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> operationDTO = accountOperations.getContent().stream().map(op -> bankAccountMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(operationDTO);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance((bankAccount.getBalance()));
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.findByNameContains(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;
    }
}
