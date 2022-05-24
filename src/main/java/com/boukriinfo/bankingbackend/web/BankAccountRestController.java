package com.boukriinfo.bankingbackend.web;

import com.boukriinfo.bankingbackend.dtos.AccountHistoryDTO;
import com.boukriinfo.bankingbackend.dtos.AccountOperationDTO;
import com.boukriinfo.bankingbackend.dtos.BankAccountDTO;
import com.boukriinfo.bankingbackend.entities.BankAccount;
import com.boukriinfo.bankingbackend.exceptions.BalanceNotSufficientException;
import com.boukriinfo.bankingbackend.exceptions.BankAccountNotFoundException;
import com.boukriinfo.bankingbackend.mappers.BankAccountMapperImpl;
import com.boukriinfo.bankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class BankAccountRestController {
    private BankAccountService bankAccountService;
    private BankAccountMapperImpl bankAccountMapper;

    @GetMapping("/accounts")
    public List<BankAccountDTO> listBankAccount(){

        return bankAccountService.listBankAccounts();
    }
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getbankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts/operations/{accountId}")
    public List<AccountOperationDTO> gethistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/pageOperations/{accountId}")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                                     @RequestParam(name = "page",defaultValue = "0") int page,
                                                     @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }

}
