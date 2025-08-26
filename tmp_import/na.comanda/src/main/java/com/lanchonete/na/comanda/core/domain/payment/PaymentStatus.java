package com.lanchonete.na.comanda.core.domain.payment;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.PaymentStatusResponse;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentStatus {

    private Long orderId;
    private PaymentStatusEnum paymentStatus;

    public PaymentStatusResponse toPaymentStatusResponse(){
        return PaymentStatusResponse.builder()
                        .orderId(orderId)
                        .paymentStatus(paymentStatus)
                        .build();
    }
    
}
