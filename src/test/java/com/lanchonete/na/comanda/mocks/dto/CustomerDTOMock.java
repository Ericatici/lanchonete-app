package com.lanchonete.na.comanda.mocks.dto;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.EMAIL;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.NAME;

import com.lanchonete.na.comanda.core.application.dto.CustomerDTO;


public class CustomerDTOMock {

    public static CustomerDTO customerDTOMock(){
        return CustomerDTO.builder()
                .cpf(CPF)
                .name(NAME)
                .email(EMAIL)
                .build();
    }
    
}
