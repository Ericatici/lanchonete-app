package com.lanchonete.na.comanda.core.domain.repositories;

import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;
import com.lanchonete.na.comanda.core.domain.payment.PaymentData;

public interface PaymentRepository {

    PaymentConfirmation getPaymentStatus(String paymentId);
    PaymentData getPaymentData(Order order);
    
}
