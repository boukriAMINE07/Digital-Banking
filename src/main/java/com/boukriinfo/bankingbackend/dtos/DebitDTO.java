package com.boukriinfo.bankingbackend.dtos;

import com.boukriinfo.bankingbackend.enums.OperationType;
import lombok.Data;

import java.util.Date;


@Data
public class DebitDTO {

     private String accountId;
     private double amount;
     private String description;
}
