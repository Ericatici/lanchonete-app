package com.lanchonete.na.comanda.adapter.driven.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;

import lombok.Data;

@Data
public class MPPaymentConfirmationResponse {

    @JsonProperty
    private String id;

    @JsonProperty
    private String status;

    @JsonProperty("total_amount")
    private Double totalAmount;
    

    public PaymentConfirmation toPaymentConfirmation(){
        return PaymentConfirmation.builder()
            .id(this.id)
            .status(this.status)
            .totalAmount(this.totalAmount)
            .build();
    }
    
}
