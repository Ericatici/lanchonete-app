package com.lanchonete.na.comanda.adapter.driver.rest.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentStatusResponse {
    @JsonProperty
    private Long orderId;
    @JsonProperty
    private PaymentStatusEnum paymentStatus;
}

