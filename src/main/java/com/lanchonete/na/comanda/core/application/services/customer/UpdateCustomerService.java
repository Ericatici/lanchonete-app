package com.lanchonete.na.comanda.core.application.services.customer;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lanchonete.na.comanda.core.application.dto.CustomerDTO;
import com.lanchonete.na.comanda.core.application.usecases.customer.UpdateCustomerUseCase;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.exeptions.CustomerNotFoundException;
import com.lanchonete.na.comanda.core.domain.repositories.CustomerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateCustomerService implements UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer updateCustomer(final CustomerDTO dto) {
        log.info("Updating customer with CPF: {}", dto.getCpf());

        Optional<Customer> customerOptional = customerRepository.findById(dto.getCpf());

        if (customerOptional.isPresent()){
            Customer existingCustomer = customerOptional.get();

            updateExistingCustomer(existingCustomer, dto);

            Customer updatedCustomer = customerRepository.updateCustomer(existingCustomer);

            log.info("Customer updated successfully with CPF: {}", updatedCustomer.getCpf());

            return updatedCustomer;
        }

        log.warn("Customer not found with CPF: {}", dto.getCpf());
        throw new CustomerNotFoundException("Customer with CPF " + dto.getCpf() + " not found"); 
    }

    private void updateExistingCustomer(Customer existingCustomer, final CustomerDTO dto){
        existingCustomer.setCpf(dto.getCpf());
        existingCustomer.setEmail(dto.getEmail());
        existingCustomer.setName(dto.getName());
    }
    
}
