package com.lanchonete.na.comanda.core.domain.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentConfirmation {

    @JsonProperty
    private String id;
    @JsonProperty
    private String status;
    @JsonProperty
    private Double totalAmount;
    
}
