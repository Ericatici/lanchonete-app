package com.lanchonete.na.comanda.core.application.services.customer;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.UPDATED_EMAIL;
import static com.lanchonete.na.comanda.mocks.dto.CustomerDTOMock.customerDTOMock;
import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.application.dto.CustomerDTO;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.exeptions.CustomerNotFoundException;
import com.lanchonete.na.comanda.core.domain.repositories.CustomerRepository;

class UpdateCustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private UpdateCustomerService updateCustomerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUpdateCustomerWhenCustomerExists() {
        final Customer customer = customerMock();

        final CustomerDTO customerDTO = customerDTOMock();
        customerDTO.setEmail(UPDATED_EMAIL);
  
        final Customer updatedCustomer = customerMock();
        updatedCustomer.setEmail(UPDATED_EMAIL);

        when(customerRepository.findById(customerDTO.getCpf())).thenReturn(Optional.of(customer));
        when(customerRepository.updateCustomer(any(Customer.class))).thenReturn(updatedCustomer);

        Customer actualResponse = updateCustomerService.updateCustomer(customerDTO);

        assertEquals(updatedCustomer, actualResponse);
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {
        final CustomerDTO customerDTO = customerDTOMock();

        when(customerRepository.findById(customerDTO.getCpf())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> updateCustomerService.updateCustomer(customerDTO));
    }
}
