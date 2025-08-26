package com.lanchonete.na.comanda.mocks.request;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.EMAIL;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.NAME;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.CustomerRequest;

public class CustomerRequestMock {
    
    public static CustomerRequest customerRequestMock(){
        return CustomerRequest.builder()
                .cpf(CPF)
                .name(NAME)
                .email(EMAIL)
                .build();
    }

}
