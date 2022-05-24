package com.boukriinfo.bankingbackend.web;

import com.boukriinfo.bankingbackend.dtos.CustomerDTO;
import com.boukriinfo.bankingbackend.entities.Customer;
import com.boukriinfo.bankingbackend.exceptions.CustomerNotFoundException;
import com.boukriinfo.bankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
        private BankAccountService bankAccountService;

        @GetMapping("/customers")
        public List<CustomerDTO> customers(){
                return bankAccountService.listCustomers();
        }
        @GetMapping("/customers/{id}")
        public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
                return bankAccountService.getCustomer(customerId);
        }

        @PostMapping("/customers")
        public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){

                return bankAccountService.saveCustomer(customerDTO);
        }
        @PutMapping("/customers/{customerId}")
        public CustomerDTO updateCustomer(@PathVariable Long customerId,@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
                customerDTO.setId(customerId);
                CustomerDTO customerDTO1 = bankAccountService.updateCustomer(customerDTO);
                return customerDTO1;
        }
        @DeleteMapping("/customers/{customerId}")
        public void deleteCustomer (@PathVariable Long customerId){
                bankAccountService.deleteCustomer(customerId);

        }
}
