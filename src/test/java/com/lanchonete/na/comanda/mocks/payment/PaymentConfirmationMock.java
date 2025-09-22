package com.lanchonete.na.comanda.mocks.payment;

import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;

public class PaymentConfirmationMock {

    public static PaymentConfirmation paymentConfirmationMock(){
        return PaymentConfirmation.builder()
            .id(null)
            .status(null)
            .totalAmount(null)
            .build();
    }
    
}
