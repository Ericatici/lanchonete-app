package com.lanchonete.na.comanda.mocks.request;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.PAYMENT_EXTERNAL_REFERENCE_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;

import java.time.Instant;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.PaymentConfirmationRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.PaymentConfirmationRequest.PaymentConfirmationDataRequest;

public class PaymentConfirmationRequestMock {

    public static PaymentConfirmationRequest paymentConfirmationRequestMock() {
        return PaymentConfirmationRequest.builder()
                .id(ORDER_ID)
                .liveMode(true)
                .type("payment")
                .dateCreated(Instant.now())
                .userId(12345L)
                .apiVersion("v1")
                .action("approved")
                .data(PaymentConfirmationDataRequest.builder()
                        .id(PAYMENT_EXTERNAL_REFERENCE_ID)
                        .build())
                .build();
    }
}
