package com.lanchonete.na.comanda.mocks.response;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.PaymentStatusResponse;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;

public class PaymentStatusResponseMock {

    public static PaymentStatusResponse paymentStatusResponseMock() {
        return PaymentStatusResponse.builder()
                .orderId(ORDER_ID)
                .paymentStatus(PaymentStatusEnum.APPROVED)
                .build();
    }

    public static PaymentStatusResponse paymentStatusResponseMock(PaymentStatusEnum status) {
        return PaymentStatusResponse.builder()
                .orderId(ORDER_ID)
                .paymentStatus(status)
                .build();
    }
}
