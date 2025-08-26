package com.lanchonete.na.comanda.adapter.driver.rest.controllers;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.INVALID_EMAIL;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.REQUEST_TRACE_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.UPDATED_EMAIL;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.UPDATED_NAME;
import static com.lanchonete.na.comanda.mocks.request.CustomerRequestMock.customerRequestMock;
import static com.lanchonete.na.comanda.mocks.response.CustomerResponseMock.customerResponseMock;
import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.CustomerRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.CustomerUpdateRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.CustomerResponse;
import com.lanchonete.na.comanda.core.application.dto.CustomerDTO;
import com.lanchonete.na.comanda.core.application.services.customer.CreateCustomerService;
import com.lanchonete.na.comanda.core.application.services.customer.FindCustomerService;
import com.lanchonete.na.comanda.core.application.services.customer.UpdateCustomerService;
import com.lanchonete.na.comanda.core.domain.customer.Customer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class CustomerControllerTest {
    
    @Mock
    private CreateCustomerService createCustomerService;

    @Mock
    private FindCustomerService findCustomerService;

    @Mock
    private UpdateCustomerService updateCustomerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void shouldCreateCustomerWhenRequestIsValid() {
        final CustomerRequest customerRequest = customerRequestMock();
        final CustomerResponse customerResponse = customerResponseMock();
        final Customer customer = customerMock();

        when(createCustomerService.createCustomer(any(CustomerDTO.class))).thenReturn(customer);

        ResponseEntity<CustomerResponse> response = customerController.createCustomer(REQUEST_TRACE_ID, customerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(customerResponse, response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() {
        final CustomerRequest invalidRequest = new CustomerRequest("", "", INVALID_EMAIL);
        final Set<ConstraintViolation<CustomerRequest>> violations = new HashSet<>();

        when(createCustomerService.createCustomer(any(CustomerDTO.class)))
                .thenThrow(new ConstraintViolationException(violations));

        try {
            customerController.createCustomer(REQUEST_TRACE_ID, invalidRequest);
        } catch (ConstraintViolationException ex) {
            assertEquals(ConstraintViolationException.class, ex.getClass());
        }
    }

    @Test
    void shouldUpdateCustomerWhenRequestIsValid() {
        final CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(UPDATED_NAME, UPDATED_EMAIL);
        final CustomerResponse updatedResponse = CustomerResponse.builder()
                .cpf(CPF)
                .name(UPDATED_NAME)
                .email(UPDATED_EMAIL)
                .build();
        final Customer updatedCustomer = Customer.builder()
                .cpf(CPF)
                .name(UPDATED_NAME)
                .email(UPDATED_EMAIL)
                .build();;

        when(updateCustomerService.updateCustomer(any(CustomerDTO.class))).thenReturn(updatedCustomer);

        ResponseEntity<CustomerResponse> response = customerController.updateCustomer(REQUEST_TRACE_ID, CPF, updateRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(updatedResponse, response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid() {
        final CustomerUpdateRequest invalidRequest = new CustomerUpdateRequest("", INVALID_EMAIL);
        final Set<ConstraintViolation<CustomerUpdateRequest>> violations = new HashSet<>();
        
        when(updateCustomerService.updateCustomer(any(CustomerDTO.class)))
                .thenThrow(new ConstraintViolationException(violations));

        try {
            customerController.updateCustomer(REQUEST_TRACE_ID, CPF, invalidRequest);
        } catch (ConstraintViolationException ex) {
            assertEquals(ConstraintViolationException.class, ex.getClass());
        }
    }

    @Test
    void shouldReturnCustomerWhenCustomerExists() {
        final CustomerResponse customerResponse = customerResponseMock();
        final Customer customer = customerMock();

        when(findCustomerService.getCustomerOptionalByCpf(CPF)).thenReturn(Optional.of(customer));

        ResponseEntity<CustomerResponse> response = customerController.getCustomerByCpf(REQUEST_TRACE_ID, CPF);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerResponse, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenCustomerDoesNotExist() {       
        when(findCustomerService.getCustomerOptionalByCpf(CPF)).thenReturn(Optional.empty());

        ResponseEntity<CustomerResponse> response = customerController.getCustomerByCpf(REQUEST_TRACE_ID, CPF);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
