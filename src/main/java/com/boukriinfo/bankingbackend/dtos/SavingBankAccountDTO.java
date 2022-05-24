package com.boukriinfo.bankingbackend.dtos;
import com.boukriinfo.bankingbackend.entities.Customer;
import com.boukriinfo.bankingbackend.enums.AccountStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
public class SavingBankAccountDTO extends BankAccountDTO {

    private String id;
    private double balance;
    private Date createDate;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;

}
