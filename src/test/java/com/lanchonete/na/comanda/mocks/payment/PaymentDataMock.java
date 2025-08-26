package com.lanchonete.na.comanda.mocks.payment;

import com.lanchonete.na.comanda.core.domain.payment.PaymentData;

public class PaymentDataMock {

    public static PaymentData paymentDataMock(){
        return PaymentData.builder()
            .qrCode(null)
            .paymentId(null)
            .build();
    }
    
}
