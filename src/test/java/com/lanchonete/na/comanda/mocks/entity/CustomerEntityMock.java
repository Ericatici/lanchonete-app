package com.lanchonete.na.comanda.mocks.entity;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.EMAIL;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.NAME;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.CustomerEntity;

public class CustomerEntityMock {
    
    public static CustomerEntity customerEntityMock(){
        return customerEntityMock(CPF);
    }

    public static CustomerEntity customerEntityMock(final String cpf){
        return CustomerEntity.builder()
                .cpf(cpf)
                .name(NAME)
                .email(EMAIL)
                .build();
    }
}
