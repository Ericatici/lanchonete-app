package com.lanchonete.na.comanda.core.domain.customer;

import java.time.Instant;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.CustomerResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String cpf;
    private String name;
    private String email;
    private Instant createdDate;
    private Instant updatedDate;

    public CustomerResponse toCustomerResponse() { 
        return CustomerResponse.builder()
                .cpf(this.cpf)
                .name(this.name)
                .email(this.email)
                .build();
    }
    
}
