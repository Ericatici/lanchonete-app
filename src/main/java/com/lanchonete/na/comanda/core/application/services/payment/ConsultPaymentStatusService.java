package com.lanchonete.na.comanda.core.application.services.payment;

import com.lanchonete.na.comanda.core.application.usecases.order.UpdateOrderPaymentStatusUseCase;
import com.lanchonete.na.comanda.core.application.usecases.payment.ConsultPaymentStatusUseCase;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.OrderNotFoundException;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;
import com.lanchonete.na.comanda.core.domain.payment.PaymentStatus;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;
import com.lanchonete.na.comanda.core.domain.repositories.PaymentRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ConsultPaymentStatusService implements ConsultPaymentStatusUseCase {

    private final OrderRepository ordersRepository;
    private final PaymentRepository paymentRepository;
    private final UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase;

    @Override
    public PaymentStatus getPaymentStatus(final Long orderId) {
        Order order = ordersRepository.findOrderById(orderId);

        if (order != null) {
            log.info("Processing payment confirmation for Order: {}", orderId);

            if (order.getPaymentStatus() != PaymentStatusEnum.PENDING) {
                log.info("Order {} in final payment status: {}", orderId, order.getPaymentStatus());

                return PaymentStatus.builder()
                    .orderId(orderId)
                    .paymentStatus(order.getPaymentStatus())
                    .build();
            }

            final PaymentConfirmation payment = paymentRepository.getPaymentStatus(order.getPaymentId());
            updateOrderPaymentStatusUseCase.updateOrderPaymentStatus(order, payment); 

            order = ordersRepository.findOrderById(orderId);

            return PaymentStatus.builder()
                    .orderId(orderId)
                    .paymentStatus(order.getPaymentStatus())
                    .build();
        } else {
            log.error("Order not found with id: {}", orderId);
            throw new OrderNotFoundException("Order with id " + orderId + " not found"); 
        }
    }
}
