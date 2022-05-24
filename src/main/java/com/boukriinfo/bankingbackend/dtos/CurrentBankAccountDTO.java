package com.boukriinfo.bankingbackend.dtos;
import com.boukriinfo.bankingbackend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;


@Data
public class CurrentBankAccountDTO extends BankAccountDTO {

    private String id;
    private double balance;
    private Date createDate;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;

}
