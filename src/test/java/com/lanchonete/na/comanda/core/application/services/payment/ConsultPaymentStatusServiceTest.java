package com.lanchonete.na.comanda.core.application.services.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.application.usecases.order.UpdateOrderPaymentStatusUseCase;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.OrderNotFoundException;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;
import com.lanchonete.na.comanda.core.domain.payment.PaymentStatus;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;
import com.lanchonete.na.comanda.core.domain.repositories.PaymentRepository;

public class ConsultPaymentStatusServiceTest {
    
    @Mock
    private OrderRepository ordersRepository;

    @Mock
    private PaymentRepository paymentRepository;
    
    @Mock
    private UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase;

    @InjectMocks
    private ConsultPaymentStatusService consultPaymentStatusService;

    private Order order;
    private PaymentConfirmation paymentConfirmation;
    private final Long ORDER_ID = 123L;
    private final String PAYMENT_ID = "payment-abc-123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        order = Order.builder()
            .id(ORDER_ID)
            .paymentId(PAYMENT_ID)
            .paymentStatus(PaymentStatusEnum.PENDING)
            .build();

        paymentConfirmation = PaymentConfirmation.builder()
            .status("processed")
            .id(PAYMENT_ID)
            .build();
    }

    @Test
    void shouldReturnFinalPaymentStatusIfOrderIsNotPending() {
        order.setPaymentStatus(PaymentStatusEnum.APPROVED);
        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);

        PaymentStatus result = consultPaymentStatusService.getPaymentStatus(ORDER_ID);

        assertNotNull(result);
        assertEquals(ORDER_ID, result.getOrderId());
        assertEquals(PaymentStatusEnum.APPROVED, result.getPaymentStatus());
        verify(ordersRepository, times(1)).findOrderById(ORDER_ID);
        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(updateOrderPaymentStatusUseCase);
    }

    @Test
    void shouldConsultAndUpdatePaymentStatusIfOrderIsPending() {
        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order); 
        when(paymentRepository.getPaymentStatus(PAYMENT_ID)).thenReturn(paymentConfirmation);

        doAnswer(invocation -> {
            Order updatedOrder = invocation.getArgument(0);
            updatedOrder.setPaymentStatus(PaymentStatusEnum.APPROVED); 
            return null;
        }).when(updateOrderPaymentStatusUseCase).updateOrderPaymentStatus(any(Order.class), any(PaymentConfirmation.class));


        when(ordersRepository.findOrderById(ORDER_ID))
            .thenReturn(order) 
            .thenReturn(order); 

        PaymentStatus result = consultPaymentStatusService.getPaymentStatus(ORDER_ID);

        assertNotNull(result);
        assertEquals(ORDER_ID, result.getOrderId());
        assertEquals(PaymentStatusEnum.APPROVED, result.getPaymentStatus()); 
        verify(ordersRepository, times(2)).findOrderById(ORDER_ID); 
        verify(paymentRepository, times(1)).getPaymentStatus(PAYMENT_ID);
        verify(updateOrderPaymentStatusUseCase, times(1)).updateOrderPaymentStatus(order, paymentConfirmation);
    }


    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderNotFound() {
        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(null);

        OrderNotFoundException thrown = assertThrows(OrderNotFoundException.class, () ->
                consultPaymentStatusService.getPaymentStatus(ORDER_ID));

        assertEquals("Order with id " + ORDER_ID + " not found", thrown.getMessage());
        verify(ordersRepository, times(1)).findOrderById(ORDER_ID);
        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(updateOrderPaymentStatusUseCase);
    }

    @Test
    void shouldHandleNullPaymentConfirmationWhenOrderIsPending() {
        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);
        when(paymentRepository.getPaymentStatus(PAYMENT_ID)).thenReturn(null); 

        doAnswer(invocation -> {
            Order updatedOrder = invocation.getArgument(0);
            PaymentConfirmation receivedPayment = invocation.getArgument(1);

            if (receivedPayment == null) {
                updatedOrder.setPaymentStatus(PaymentStatusEnum.REJECTED); 
            }
            return null;
        }).when(updateOrderPaymentStatusUseCase).updateOrderPaymentStatus(any(Order.class), isNull());

        when(ordersRepository.findOrderById(ORDER_ID))
            .thenReturn(order) 
            .thenReturn(order); 

        PaymentStatus result = consultPaymentStatusService.getPaymentStatus(ORDER_ID);

        assertNotNull(result);
        assertEquals(ORDER_ID, result.getOrderId());
        assertEquals(PaymentStatusEnum.REJECTED, result.getPaymentStatus()); 
        verify(ordersRepository, times(2)).findOrderById(ORDER_ID);
        verify(paymentRepository, times(1)).getPaymentStatus(PAYMENT_ID);
        verify(updateOrderPaymentStatusUseCase, times(1)).updateOrderPaymentStatus(order, null);
    }
}
