package com.lanchonete.na.comanda.core.application.usecases.payment;

import com.lanchonete.na.comanda.core.domain.payment.PaymentStatus;

public interface ConsultPaymentStatusUseCase {
    PaymentStatus getPaymentStatus(Long orderId);
}
