package com.lanchonete.na.comanda.mocks.dto;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.PAYMENT_EXTERNAL_REFERENCE_ID;

import java.time.Instant;

import com.lanchonete.na.comanda.core.application.dto.PaymentConfirmationDTO;
import com.lanchonete.na.comanda.core.application.dto.PaymentConfirmationDTO.PaymentConfirmationDataDTO;


public class PaymentConfirmationDTOMock {

    public static PaymentConfirmationDTO paymentConfirmationDTOMock() {
        return PaymentConfirmationDTO.builder()
                .id(ORDER_ID)
                .liveMode(true)
                .type("payment")
                .dateCreated(Instant.now())
                .userId(12345L)
                .apiVersion("v1")
                .action("approved")
                .data(PaymentConfirmationDataDTO.builder()
                        .id(PAYMENT_EXTERNAL_REFERENCE_ID)
                        .build())
                .build();
    }
    
}
