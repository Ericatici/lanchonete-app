package com.lanchonete.na.comanda.adapter.driver.rest.controllers.mvc;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.EMAIL;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.INVALID_EMAIL;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.NAME;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.UPDATED_EMAIL;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.UPDATED_NAME;
import static com.lanchonete.na.comanda.mocks.request.CustomerRequestMock.customerRequestMock;
import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.CustomerController;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.CustomerRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.CustomerUpdateRequest;
import com.lanchonete.na.comanda.core.application.services.customer.CreateCustomerService;
import com.lanchonete.na.comanda.core.application.services.customer.FindCustomerService;
import com.lanchonete.na.comanda.core.application.services.customer.UpdateCustomerService;
import com.lanchonete.na.comanda.core.domain.customer.Customer;


@WebMvcTest(CustomerController.class)
class CustomerControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateCustomerService createCustomerService;

    @MockitoBean
    private FindCustomerService findCustomerService;

    @MockitoBean
    private UpdateCustomerService updateCustomerService;

    @Autowired
    private ObjectMapper objectMapper;


     @Test
    void shouldCreateCustomerWhenInputIsValid() throws Exception {
        CustomerRequest request = customerRequestMock();
        final Customer customer = customerMock();

        when(createCustomerService.createCustomer(any())).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.post("/customer/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(CPF))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(EMAIL));
    }

    @Test
    void shouldReturnBadRequestWhenCpfIsInvalid() throws Exception {
        CustomerRequest request = customerRequestMock();
        request.setCpf("123");

        mockMvc.perform(MockMvcRequestBuilders.post("/customer/save")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        CustomerRequest request = customerRequestMock();
        request.setName("");

        mockMvc.perform(MockMvcRequestBuilders.post("/customer/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        CustomerRequest request = customerRequestMock();
        request.setEmail(INVALID_EMAIL);
  
        mockMvc.perform(MockMvcRequestBuilders.post("/customer/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldUpdateCustomerWhenInputIsValid() throws Exception {
        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .name(UPDATED_NAME)
                .email(UPDATED_EMAIL)
                .build();

        final Customer customer = customerMock();
        customer.setEmail(UPDATED_EMAIL);
        customer.setName(UPDATED_NAME);

        when(updateCustomerService.updateCustomer(any())).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.put("/customer/{id}", CPF)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(UPDATED_EMAIL));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateNameIsBlank() throws Exception {
        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .name("")
                .email(UPDATED_EMAIL)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/customer/{id}", CPF)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenUpdateEmailIsInvalid() throws Exception {
        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .name(UPDATED_NAME)
                .email(INVALID_EMAIL)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/customer/{id}", CPF)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldGetCustomerByCpfWhenCustomerExists() throws Exception {
        Customer customer = customerMock();

        when(findCustomerService.getCustomerOptionalByCpf(CPF)).thenReturn(Optional.of(customer));

        mockMvc.perform(MockMvcRequestBuilders.get("/customer/{cpf}", CPF)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(CPF))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(EMAIL));
    }

    @Test
    void shouldReturnNotFoundWhenCustomerDoesNotExist() throws Exception {
        when(findCustomerService.getCustomerOptionalByCpf(CPF)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/customer/{cpf}", CPF)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}

