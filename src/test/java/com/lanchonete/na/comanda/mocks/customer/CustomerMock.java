package com.lanchonete.na.comanda.mocks.customer;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.EMAIL;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.NAME;

import java.time.Instant;

import com.lanchonete.na.comanda.core.domain.customer.Customer;

public class CustomerMock {

    public static Customer customerMock(){
        return customerMock(CPF);
    }
    

    public static Customer customerMock(final String cpf){
        return Customer.builder()
                .cpf(cpf)
                .name(NAME)
                .email(EMAIL)
                .build();
    }

    public static Customer customerWithDatesMock(){
        return Customer.builder()
                .cpf(CPF)
                .name(NAME)
                .email(EMAIL)
                .createdDate(Instant.now())
                .updatedDate(Instant.now())
                .build();
    }
    
}
