package com.lanchonete.na.comanda.mocks.response;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.EMAIL;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.NAME;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.CustomerResponse;

public class CustomerResponseMock {

    public static CustomerResponse customerResponseMock(){
        return CustomerResponse.builder()
                .cpf(CPF)
                .name(NAME)
                .email(EMAIL)
                .build();
    }
    
}
