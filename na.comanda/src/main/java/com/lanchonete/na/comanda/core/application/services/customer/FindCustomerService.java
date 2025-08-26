package com.lanchonete.na.comanda.core.application.services.customer;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lanchonete.na.comanda.core.application.usecases.customer.FindCustomerUseCase;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.exeptions.CustomerNotFoundException;
import com.lanchonete.na.comanda.core.domain.repositories.CustomerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class FindCustomerService implements FindCustomerUseCase {
    
    private final CustomerRepository customerRepository;
    
    @Override
    public Optional<Customer> getCustomerOptionalByCpf(final String cpf) {
        log.info("Finding customer with CPF: {}", cpf);

        return customerRepository.findById(cpf);
    }

    @Override
    public Customer getCustomerByCpf(final String cpf) {
        log.info("Finding customer with CPF: {}", cpf);

        Optional<Customer> customerOptional = Optional.ofNullable(customerRepository.findById(cpf)
        .orElseThrow(() -> new CustomerNotFoundException("Customer with CPF " + cpf + " not found")));

        return customerOptional.get();
    }
    
}
