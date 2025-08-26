package com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.CustomerEntity;
import com.lanchonete.na.comanda.core.domain.customer.Customer;

public class CustomerEntityMapper {

    public static CustomerEntity fromCustomerToCustomerEntity (final Customer customer){
        CustomerEntity customerEntity = CustomerEntity.builder()
                .cpf(customer.getCpf())
                .email(customer.getEmail())
                .name(customer.getName())
                .build();

        if (customer.getCreatedDate() != null) {
            customerEntity.setCreatedDate(customer.getCreatedDate());
        }

        if (customer.getUpdatedDate() != null) {
            customerEntity.setUpdatedDate(customer.getUpdatedDate());
        }

        return customerEntity;
    }
    
}
