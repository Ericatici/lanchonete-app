package com.lanchonete.na.comanda.core.application.services.payment;

import com.lanchonete.na.comanda.core.application.dto.PaymentConfirmationDTO;
import com.lanchonete.na.comanda.core.application.usecases.order.UpdateOrderPaymentStatusUseCase;
import com.lanchonete.na.comanda.core.application.usecases.payment.ProcessPaymentWebhookUseCase;
import com.lanchonete.na.comanda.core.domain.exeptions.InvalidPaymentException;
import com.lanchonete.na.comanda.core.domain.exeptions.OrderNotFoundException;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;
import com.lanchonete.na.comanda.core.domain.repositories.PaymentRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@AllArgsConstructor
public class ProcessPaymentWebhookService implements ProcessPaymentWebhookUseCase {

    private final OrderRepository ordersRepository;
    private final PaymentRepository paymentRepository;
    private final UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase;

    @Override
    @Transactional
    public void processPaymentConfirmation(final PaymentConfirmationDTO paymentConfirmationDTO) {
        final String paymentId = paymentConfirmationDTO.getData().getId();
   
        final Order order = ordersRepository.findOrderByPaymentId(paymentId);

        if (order != null) {
            log.info("Processing payment confirmation for payment ID: {}", paymentId);

            final PaymentConfirmation payment = paymentRepository.getPaymentStatus(paymentId);

            if (payment != null) {
                updateOrderPaymentStatusUseCase.updateOrderPaymentStatus(order, payment);    
            } else {
                log.error("No payment confirmation received for paymentId {}", paymentId);
                throw new InvalidPaymentException("No payment confirmation received for paymentId " + paymentId);
            }
        } else {
            log.error("No order with paymentId {} found in the database", paymentId);
            throw new OrderNotFoundException("Order with paymentId " + paymentId + " not found"); 
        }
    }  
}
