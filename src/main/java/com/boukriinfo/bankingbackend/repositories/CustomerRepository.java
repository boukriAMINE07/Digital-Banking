package com.boukriinfo.bankingbackend.repositories;

import com.boukriinfo.bankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
