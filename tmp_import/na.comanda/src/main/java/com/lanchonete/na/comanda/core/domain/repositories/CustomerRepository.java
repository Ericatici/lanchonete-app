package com.lanchonete.na.comanda.core.domain.repositories;

import java.util.Optional;

import com.lanchonete.na.comanda.core.domain.customer.Customer;

public interface CustomerRepository {

    Optional<Customer> findById(String cpf);
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    
}
