package com.lanchonete.na.comanda.core.application.usecases.payment;

import com.lanchonete.na.comanda.core.application.dto.PaymentConfirmationDTO;

public interface ProcessPaymentWebhookUseCase {
    void processPaymentConfirmation(PaymentConfirmationDTO paymentConfirmationDTO);
}
