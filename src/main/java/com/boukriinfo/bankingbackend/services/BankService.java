package com.boukriinfo.bankingbackend.services;

import com.boukriinfo.bankingbackend.entities.BankAccount;
import com.boukriinfo.bankingbackend.entities.CurrentAccount;
import com.boukriinfo.bankingbackend.entities.SavingAccount;
import com.boukriinfo.bankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void consulter(){
        BankAccount bankAccount=
                bankAccountRepository.findById("0e54d799-1e10-46c8-855e-2083df636c93").orElse(null);

        System.out.println("******** Affichage ******");
        System.out.println(bankAccount.getStatus());
        System.out.println(bankAccount.getBalance());
        System.out.println(bankAccount.getCustomer().getName());
        System.out.println(bankAccount.getCreateDate());
        System.out.println(bankAccount.getAccountOperations());
        System.out.println(bankAccount.getClass().getSimpleName());

        if(bankAccount instanceof CurrentAccount){
            System.out.println("Over Draft"+((CurrentAccount) bankAccount).getOverDraft());
        }else  if(bankAccount instanceof SavingAccount){
            System.out.println("Interest Rate"+((SavingAccount) bankAccount).getInterestRate());
        }

        bankAccount.getAccountOperations().forEach(op->{
            System.out.println(op.getType()+"\t"+op.getOperationDate()+"\t"+op.getBankAccount());
        });
    }
}
