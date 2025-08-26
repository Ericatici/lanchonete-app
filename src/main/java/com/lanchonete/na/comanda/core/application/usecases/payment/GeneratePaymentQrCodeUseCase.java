package com.lanchonete.na.comanda.core.application.usecases.payment;

import com.lanchonete.na.comanda.core.domain.order.Order;

public interface GeneratePaymentQrCodeUseCase {
    Order generatePaymentQrCode(Order order);
}
