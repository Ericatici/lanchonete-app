package com.lanchonete.na.comanda.core.application.services.payment;

import org.springframework.stereotype.Service;

import com.lanchonete.na.comanda.core.application.usecases.payment.GeneratePaymentQrCodeUseCase;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentData;
import com.lanchonete.na.comanda.core.domain.repositories.PaymentRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GeneratePaymentQrCodeService implements GeneratePaymentQrCodeUseCase {

    private final PaymentRepository paymentRepository;

    @Override
    public Order generatePaymentQrCode(Order order) {
        log.info("Getting payment data information");
        final PaymentData paymentData = paymentRepository.getPaymentData(order);
        
        if (paymentData != null && paymentData.getPaymentId() != null && paymentData.getQrCode() != null) {
            log.info("Updating order with payment data information");
            order.setPaymentId(paymentData.getPaymentId());
            order.setQrCodeData(paymentData.getQrCode());
            return order;
        }

        log.warn("Order cancelled due to invalid payment data information");
        order.setPaymentStatus(PaymentStatusEnum.REJECTED);
        order.setStatus(OrderStatusEnum.CANCELLED);
        return order;
    }
    
}
