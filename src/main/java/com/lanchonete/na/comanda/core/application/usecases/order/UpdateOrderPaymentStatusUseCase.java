package com.lanchonete.na.comanda.core.application.usecases.order;

import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;

public interface UpdateOrderPaymentStatusUseCase {

    void updateOrderPaymentStatus(Order order, PaymentConfirmation payment);
    
}
