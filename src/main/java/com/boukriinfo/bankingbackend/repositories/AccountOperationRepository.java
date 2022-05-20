package com.boukriinfo.bankingbackend.repositories;

import com.boukriinfo.bankingbackend.entities.AccountOperation;
import com.boukriinfo.bankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
