package com.lanchonete.na.comanda.adapter.driven.persistence.repositories;

import java.util.Optional;

import org.springframework.stereotype.Component;

import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.CustomerEntityMapper.fromCustomerToCustomerEntity;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.CustomerEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.repositories.jpa.JpaCustomerRepository;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.repositories.CustomerRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final JpaCustomerRepository jpaCustomerRepository;

    @Override
    public Optional<Customer> findById(final String cpf) {
        Optional<CustomerEntity> customerOptional = jpaCustomerRepository.findById(cpf);

        

        return customerOptional.map(CustomerEntity::toCustomer);
    }

    @Override
    public Customer saveCustomer(final Customer customer) {
        final CustomerEntity customerEntity = fromCustomerToCustomerEntity(customer);

        final CustomerEntity createdCustomer = jpaCustomerRepository.save(customerEntity); 

        return createdCustomer.toCustomer();
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        final CustomerEntity customerEntity = fromCustomerToCustomerEntity(customer);

        final CustomerEntity createdCustomer = jpaCustomerRepository.save(customerEntity); 

        return createdCustomer.toCustomer();
    }
    
}
