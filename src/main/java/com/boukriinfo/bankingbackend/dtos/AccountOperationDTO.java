package com.boukriinfo.bankingbackend.dtos;

import com.boukriinfo.bankingbackend.enums.OperationType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
public class AccountOperationDTO {

     private Long id;
     private Date operationDate;
     private double amount;
     private OperationType type;
     private String description;

}
