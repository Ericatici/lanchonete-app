package com.lanchonete.na.comanda.core.application.services.customer;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.exeptions.CustomerNotFoundException;
import com.lanchonete.na.comanda.core.domain.repositories.CustomerRepository;

class FindCustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private FindCustomerService findCustomerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCustomerResponseWhenCustomerExists() {
        final Customer customer = customerMock();

        when(customerRepository.findById(CPF)).thenReturn(Optional.of(customer));

        Optional<Customer> actualResponse = findCustomerService.getCustomerOptionalByCpf(CPF);

        assertTrue(actualResponse.isPresent());
        assertEquals(customer, actualResponse.get());
    }

    @Test
    void shouldReturnCustomerEntityWhenCustomerExists() {
        final Customer customer = customerMock();

        when(customerRepository.findById(CPF)).thenReturn(Optional.of(customer));

        Customer actualEntity = findCustomerService.getCustomerByCpf(CPF);

        assertEquals(customer, actualEntity);
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExistGetCustomerEntityByCpf() {
        when(customerRepository.findById(CPF)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> findCustomerService.getCustomerByCpf(CPF));
    }
}