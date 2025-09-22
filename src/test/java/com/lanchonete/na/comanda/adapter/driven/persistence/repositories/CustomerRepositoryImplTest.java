package com.lanchonete.na.comanda.adapter.driven.persistence.repositories;

import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static com.lanchonete.na.comanda.mocks.entity.CustomerEntityMock.customerEntityMock;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.CustomerEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.repositories.jpa.JpaCustomerRepository;
import com.lanchonete.na.comanda.core.domain.customer.Customer;

class CustomerRepositoryImplTest {

    @Mock
    private JpaCustomerRepository jpaCustomerRepository;

    @InjectMocks
    private CustomerRepositoryImpl customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindCustomerById() {
        final Customer customer = customerMock();
        final CustomerEntity customerEntity = customerEntityMock();

        when(jpaCustomerRepository.findById(CPF)).thenReturn(Optional.of(customerEntity));

        Optional<Customer> foundCustomer = customerRepository.findById(CPF);

        assertTrue(foundCustomer.isPresent());
        assertEquals(customer.getCpf(), foundCustomer.get().getCpf());
        assertEquals(customer.getName(), foundCustomer.get().getName());
    }

    @Test
    void shouldSaveCustomer() {
        final Customer customer = customerMock();
        final CustomerEntity customerEntity = customerEntityMock();

        when(jpaCustomerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);

        Customer savedCustomer = customerRepository.saveCustomer(customer);

        assertNotNull(savedCustomer);
        assertEquals(customer.getCpf(), savedCustomer.getCpf());
        assertEquals(customer.getName(), savedCustomer.getName());
    }

    @Test
    void shouldUpdateCustomer() {
        final Customer customer = customerMock();
        final CustomerEntity customerEntity = customerEntityMock();

        when(jpaCustomerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);

        Customer updatedCustomer = customerRepository.updateCustomer(customer);

        assertNotNull(updatedCustomer);
        assertEquals(customer.getCpf(), updatedCustomer.getCpf());
        assertEquals(customer.getName(), updatedCustomer.getName());
    }
}
