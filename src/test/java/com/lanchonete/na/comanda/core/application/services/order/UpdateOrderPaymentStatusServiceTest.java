package com.lanchonete.na.comanda.core.application.services.order;

import static com.lanchonete.na.comanda.core.application.constants.ApiContants.CANCELED_STATUS;
import static com.lanchonete.na.comanda.core.application.constants.ApiContants.EXPIRED_STATUS;
import static com.lanchonete.na.comanda.core.application.constants.ApiContants.PROCESSED_STATUS;
import static com.lanchonete.na.comanda.core.application.constants.ApiContants.REFUNDED_STATUS;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static com.lanchonete.na.comanda.mocks.payment.PaymentConfirmationMock.paymentConfirmationMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;

public class UpdateOrderPaymentStatusServiceTest {
    @InjectMocks
    private UpdateOrderPaymentStatusService updateOrderPaymentStatusService;

    @Mock
    private OrderRepository ordersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUpdateStatusToApprovedAndReadyWhenPaymentIsProcessed() {
        Order order = orderMock();
        order.setPaymentStatus(PaymentStatusEnum.PENDING); 
        order.setStatus(OrderStatusEnum.WAITING_PAYMENT); 

        PaymentConfirmation payment = paymentConfirmationMock();
        payment.setStatus(PROCESSED_STATUS);

        updateOrderPaymentStatusService.updateOrderPaymentStatus(order, payment);

        assertEquals(PaymentStatusEnum.APPROVED, order.getPaymentStatus());
        assertEquals(OrderStatusEnum.READY, order.getStatus()); 
        verify(ordersRepository, times(1)).updateOrder(order);
    }

    @Test
    void shouldUpdateStatusToRejectedAndCancelledWhenPaymentIsRefunded() {
        Order order = orderMock();
        order.setPaymentStatus(PaymentStatusEnum.PENDING);
        order.setStatus(OrderStatusEnum.WAITING_PAYMENT);

        PaymentConfirmation payment = paymentConfirmationMock();
        payment.setStatus(REFUNDED_STATUS);

        updateOrderPaymentStatusService.updateOrderPaymentStatus(order, payment);

        assertEquals(PaymentStatusEnum.REJECTED, order.getPaymentStatus());
        assertEquals(OrderStatusEnum.CANCELLED, order.getStatus());
        verify(ordersRepository, times(1)).updateOrder(order);
    }

    @Test
    void shouldUpdateStatusToRejectedAndCancelledWhenPaymentIsExpired() {
        Order order = orderMock();
        order.setPaymentStatus(PaymentStatusEnum.PENDING);
        order.setStatus(OrderStatusEnum.WAITING_PAYMENT);

        PaymentConfirmation payment = paymentConfirmationMock();
        payment.setStatus(EXPIRED_STATUS);

        updateOrderPaymentStatusService.updateOrderPaymentStatus(order, payment);

        assertEquals(PaymentStatusEnum.REJECTED, order.getPaymentStatus());
        assertEquals(OrderStatusEnum.CANCELLED, order.getStatus());
        verify(ordersRepository, times(1)).updateOrder(order);
    }

    @Test
    void shouldUpdateStatusToRejectedAndCancelledWhenPaymentIsCancelled() {
        Order order = orderMock();
        order.setPaymentStatus(PaymentStatusEnum.PENDING);
        order.setStatus(OrderStatusEnum.WAITING_PAYMENT);

        PaymentConfirmation payment = paymentConfirmationMock();
        payment.setStatus(CANCELED_STATUS);

        updateOrderPaymentStatusService.updateOrderPaymentStatus(order, payment);

        assertEquals(PaymentStatusEnum.REJECTED, order.getPaymentStatus());
        assertEquals(OrderStatusEnum.CANCELLED, order.getStatus());
        verify(ordersRepository, times(1)).updateOrder(order);
    }

    @Test
    void shouldSetStatusToPendingWhenPaymentStatusIsUnknown() {
        Order order = orderMock();
        order.setPaymentStatus(PaymentStatusEnum.PENDING); 
        order.setStatus(OrderStatusEnum.WAITING_PAYMENT);

        PaymentConfirmation payment = paymentConfirmationMock();
        payment.setStatus("UNKNOWN_STATUS"); 

        updateOrderPaymentStatusService.updateOrderPaymentStatus(order, payment);

        assertEquals(PaymentStatusEnum.PENDING, order.getPaymentStatus());
        assertEquals(OrderStatusEnum.WAITING_PAYMENT, order.getStatus()); 
        verify(ordersRepository, never()).updateOrder(any(Order.class)); 
    }

    @Test
    void shouldNotUpdateOrderIfPaymentStatusIsAlreadyTheSame() {
        Order order = orderMock();
        order.setPaymentStatus(PaymentStatusEnum.APPROVED); 
        order.setStatus(OrderStatusEnum.READY);

        PaymentConfirmation payment = paymentConfirmationMock();
        payment.setStatus(PROCESSED_STATUS); 

        updateOrderPaymentStatusService.updateOrderPaymentStatus(order, payment);

        assertEquals(PaymentStatusEnum.APPROVED, order.getPaymentStatus());
        assertEquals(OrderStatusEnum.READY, order.getStatus());
        verify(ordersRepository, never()).updateOrder(any(Order.class)); 
    }

    @Test
    void shouldNotProcessUpdateWhenPaymentIsNull() {
        Order order = orderMock();
        updateOrderPaymentStatusService.updateOrderPaymentStatus(order, null);
        verify(ordersRepository, never()).updateOrder(any(Order.class));
    }

    @Test
    void shouldNotProcessUpdateWhenPaymentStatusIsNull() {
        Order order = orderMock();
        PaymentConfirmation payment = paymentConfirmationMock();
        payment.setStatus(null);
        updateOrderPaymentStatusService.updateOrderPaymentStatus(order, payment);
        verify(ordersRepository, never()).updateOrder(any(Order.class));
    }
}
