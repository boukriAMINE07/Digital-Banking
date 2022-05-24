package com.boukriinfo.bankingbackend.exceptions;

public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException(String customer_not_found) {
         super(customer_not_found);
    }
}
