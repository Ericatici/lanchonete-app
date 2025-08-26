package com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerWithDatesMock;


import org.junit.jupiter.api.Test;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.CustomerEntity;
import com.lanchonete.na.comanda.core.domain.customer.Customer;

class CustomerEntityMapperTest {

    @Test
    void shouldMapCustomerToCustomerEntity() {
        final Customer customer = customerMock(); 

        final CustomerEntity customerEntity = CustomerEntityMapper.fromCustomerToCustomerEntity(customer);

        assertEquals(customer.getCpf(), customerEntity.getCpf());
        assertEquals(customer.getName(), customerEntity.getName());
        assertEquals(customer.getEmail(), customerEntity.getEmail());
        assertNull(customerEntity.getCreatedDate());
        assertNull(customerEntity.getUpdatedDate());
    }

    @Test
    void shouldMapCustomerToCustomerEntityWithDates() {
        final Customer customer = customerWithDatesMock(); 

        final CustomerEntity customerEntity = CustomerEntityMapper.fromCustomerToCustomerEntity(customer);

        assertEquals(customer.getCpf(), customerEntity.getCpf());
        assertEquals(customer.getName(), customerEntity.getName());
        assertEquals(customer.getEmail(), customerEntity.getEmail());
        assertEquals(customer.getCreatedDate(), customerEntity.getCreatedDate());
        assertEquals(customer.getUpdatedDate(), customerEntity.getUpdatedDate());
    }
}
