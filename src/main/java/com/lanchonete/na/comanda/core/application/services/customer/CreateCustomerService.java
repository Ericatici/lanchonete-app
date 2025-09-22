package com.lanchonete.na.comanda.core.application.services.customer;

import org.springframework.stereotype.Service;

import com.lanchonete.na.comanda.core.application.dto.CustomerDTO;
import com.lanchonete.na.comanda.core.application.usecases.customer.CreateCustomerUseCase;
import com.lanchonete.na.comanda.core.application.usecases.customer.FindCustomerUseCase;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.exeptions.CustomerAlreadyExistsException;
import com.lanchonete.na.comanda.core.domain.repositories.CustomerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CreateCustomerService implements CreateCustomerUseCase{

    private final CustomerRepository customerRepository;
    private final FindCustomerUseCase findCustomerUseCase;

    @Override
    @Transactional
    public Customer createCustomer(final CustomerDTO customerDTO) {
        log.info("Creating customer with CPF: {}", customerDTO.getCpf());
        
        if (findCustomerUseCase.getCustomerOptionalByCpf(customerDTO.getCpf()).isPresent()) {
            throw new CustomerAlreadyExistsException("Customer with CPF " + customerDTO.getCpf() + " already exists");
        }
  
        return customerRepository.saveCustomer(customerDTO.toCustomer()); 
    }
    
}
