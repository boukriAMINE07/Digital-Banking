package com.boukriinfo.bankingbackend.repositories;

import com.boukriinfo.bankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
