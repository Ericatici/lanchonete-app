package com.lanchonete.na.comanda.mocks.payment;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;

import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;
import com.lanchonete.na.comanda.core.domain.payment.PaymentStatus;

public class PaymentStatusMock {

    public static PaymentStatus paymentStatusMock() {
        return PaymentStatus.builder()
                .orderId(ORDER_ID)
                .paymentStatus(PaymentStatusEnum.APPROVED)
                .build();
    }

    public static PaymentStatus paymentStatusMock(PaymentStatusEnum status) {
        return PaymentStatus.builder()
                .orderId(ORDER_ID)
                .paymentStatus(status)
                .build();
    }
    
}
