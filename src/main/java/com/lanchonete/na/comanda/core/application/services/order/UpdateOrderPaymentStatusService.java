package com.lanchonete.na.comanda.core.application.services.order;

import static com.lanchonete.na.comanda.core.application.constants.ApiContants.CANCELED_STATUS;
import static com.lanchonete.na.comanda.core.application.constants.ApiContants.EXPIRED_STATUS;
import static com.lanchonete.na.comanda.core.application.constants.ApiContants.PROCESSED_STATUS;
import static com.lanchonete.na.comanda.core.application.constants.ApiContants.REFUNDED_STATUS;

import org.springframework.stereotype.Service;

import com.lanchonete.na.comanda.core.application.usecases.order.UpdateOrderPaymentStatusUseCase;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateOrderPaymentStatusService implements UpdateOrderPaymentStatusUseCase {

    private final OrderRepository ordersRepository;

    @Override
    public void updateOrderPaymentStatus(final Order order, final PaymentConfirmation payment) {
        if (payment !=null && payment.getStatus() != null){
            PaymentStatusEnum newPaymentStatus;
            switch (payment.getStatus().toLowerCase()) {
                case PROCESSED_STATUS:
                    newPaymentStatus = PaymentStatusEnum.APPROVED;
                    order.setStatus(OrderStatusEnum.READY); 
                    log.info("Order {} status updated to IN_PREPARATION due to payment approval.", order.getId());
                    break;
                case REFUNDED_STATUS:
                case EXPIRED_STATUS:
                case CANCELED_STATUS:
                    newPaymentStatus = PaymentStatusEnum.REJECTED;
                    order.setStatus(OrderStatusEnum.CANCELLED);
                    log.warn("Order {} status updated to CANCELLED due to payment rejection.", order.getId());
                    break;
                default:
                    newPaymentStatus = PaymentStatusEnum.PENDING; 
                    log.warn("Unknown payment status received for order {}: {}", order.getId(), payment.getStatus());
                    break;
            }

            if (order.getPaymentStatus() != newPaymentStatus) {
                order.setPaymentStatus(newPaymentStatus);
                ordersRepository.updateOrder(order);
                log.info("Payment status for order {} updated to {}.", order.getId(), newPaymentStatus);
            }

        }
    } 
}
