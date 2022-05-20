package com.boukriinfo.bankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue("SA")
@AllArgsConstructor
@NoArgsConstructor
public class SavingAccount extends  BankAccount{
        private double interestRate;
}
