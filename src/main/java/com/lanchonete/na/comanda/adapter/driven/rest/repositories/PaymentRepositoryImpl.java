package com.lanchonete.na.comanda.adapter.driven.rest.repositories;

import org.springframework.stereotype.Component;

import com.lanchonete.na.comanda.adapter.driven.rest.repositories.mercadopago.MercadoPagoGatewayRepository;
import com.lanchonete.na.comanda.adapter.driven.rest.response.MPPaymentConfirmationResponse;
import com.lanchonete.na.comanda.adapter.driven.rest.response.MPQrCodePaymentResponse;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;
import com.lanchonete.na.comanda.core.domain.payment.PaymentData;
import com.lanchonete.na.comanda.core.domain.repositories.PaymentRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final MercadoPagoGatewayRepository mercadoPagoGateway;

    @Override
    public PaymentConfirmation getPaymentStatus(final String paymentId) {
        final MPPaymentConfirmationResponse response = mercadoPagoGateway.getPaymentConfirmation(paymentId);

        PaymentConfirmation payment = null;
        if (response != null) {
            payment =  response.toPaymentConfirmation();
        } 

        return payment;
    }

    @Override
    public PaymentData getPaymentData(final Order order) {
        final MPQrCodePaymentResponse response = mercadoPagoGateway.createQrCodeForPayment(order.getId(), order.getTotalPrice());

        PaymentData paymentData = null;
        if (response != null) {
            paymentData =  response.toPaymentData();
        } 

        return paymentData;
    }
    
}
