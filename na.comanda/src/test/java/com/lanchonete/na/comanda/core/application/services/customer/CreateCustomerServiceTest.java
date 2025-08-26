package com.lanchonete.na.comanda.core.application.services.customer;

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
import com.lanchonete.na.comanda.core.application.usecases.customer.FindCustomerUseCase;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.exeptions.CustomerAlreadyExistsException;
import com.lanchonete.na.comanda.core.domain.repositories.CustomerRepository;

class CreateCustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FindCustomerUseCase findCustomerUseCase;

    @InjectMocks
    private CreateCustomerService createCustomerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateCustomerWhenCustomerDoesNotExist() {
        final CustomerDTO customerDTO = customerDTOMock();
        final Customer customer = customerMock();

        when(findCustomerUseCase.getCustomerOptionalByCpf(customerDTO.getCpf())).thenReturn(Optional.empty());
        when(customerRepository.saveCustomer(any(Customer.class))).thenReturn(customer);

        Customer actualResponse = createCustomerService.createCustomer(customerDTO);

        assertEquals(customer, actualResponse);
    }

    @Test
    void shouldThrowExceptionWhenCustomerAlreadyExists() {
        final CustomerDTO customerDTO = customerDTOMock();

        when(findCustomerUseCase.getCustomerOptionalByCpf(customerDTO.getCpf())).thenReturn(Optional.of(customerDTO.toCustomer()));

        assertThrows(CustomerAlreadyExistsException.class, () -> createCustomerService.createCustomer(customerDTO));
    }
}