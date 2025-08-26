package com.lanchonete.na.comanda.core.application.services.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentData;
import com.lanchonete.na.comanda.core.domain.repositories.PaymentRepository;

public class GeneratePaymentQrCodeServiceTest {
    
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private GeneratePaymentQrCodeService generatePaymentQrCodeService;

    private Order order;
    private PaymentData paymentData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order = Order.builder()
            .id(1L)
            .paymentStatus(PaymentStatusEnum.PENDING)
            .status(OrderStatusEnum.WAITING_PAYMENT)
            .build();

        paymentData = PaymentData.builder()
            .paymentId("new-payment-id-123")
            .qrCode("qr-code-data-abc")
            .build();
    }

    @Test
    void shouldGeneratePaymentQrCodeAndUpdateOrderSuccessfully() {
        when(paymentRepository.getPaymentData(any(Order.class))).thenReturn(paymentData);

        Order updatedOrder = generatePaymentQrCodeService.generatePaymentQrCode(order);

        assertNotNull(updatedOrder);
        assertEquals("new-payment-id-123", updatedOrder.getPaymentId());
        assertEquals("qr-code-data-abc", updatedOrder.getQrCodeData());
        assertEquals(PaymentStatusEnum.PENDING, updatedOrder.getPaymentStatus()); 
        assertEquals(OrderStatusEnum.WAITING_PAYMENT, updatedOrder.getStatus());
        verify(paymentRepository, times(1)).getPaymentData(order);
    }

    @Test
    void shouldCancelOrderWhenPaymentDataIsNull() {
        when(paymentRepository.getPaymentData(any(Order.class))).thenReturn(null);

        Order updatedOrder = generatePaymentQrCodeService.generatePaymentQrCode(order);

        assertNotNull(updatedOrder);
        assertNull(updatedOrder.getPaymentId()); 
        assertNull(updatedOrder.getQrCodeData()); 
        assertEquals(PaymentStatusEnum.REJECTED, updatedOrder.getPaymentStatus());
        assertEquals(OrderStatusEnum.CANCELLED, updatedOrder.getStatus());
        verify(paymentRepository, times(1)).getPaymentData(order);
    }

    @Test
    void shouldCancelOrderWhenPaymentIdIsNull() {
        paymentData.setPaymentId(null);
        when(paymentRepository.getPaymentData(any(Order.class))).thenReturn(paymentData);

        Order updatedOrder = generatePaymentQrCodeService.generatePaymentQrCode(order);

        assertNotNull(updatedOrder);
        assertNull(updatedOrder.getPaymentId());
        assertNull(updatedOrder.getQrCodeData()); 
        assertEquals(PaymentStatusEnum.REJECTED, updatedOrder.getPaymentStatus());
        assertEquals(OrderStatusEnum.CANCELLED, updatedOrder.getStatus());
        verify(paymentRepository, times(1)).getPaymentData(order);
    }

    @Test
    void shouldCancelOrderWhenQrCodeIsNull() {
        paymentData.setQrCode(null);
        when(paymentRepository.getPaymentData(any(Order.class))).thenReturn(paymentData);

        Order updatedOrder = generatePaymentQrCodeService.generatePaymentQrCode(order);

        assertNotNull(updatedOrder);
        assertNull(updatedOrder.getPaymentId());
        assertNull(updatedOrder.getQrCodeData());
        assertEquals(PaymentStatusEnum.REJECTED, updatedOrder.getPaymentStatus());
        assertEquals(OrderStatusEnum.CANCELLED, updatedOrder.getStatus());
        verify(paymentRepository, times(1)).getPaymentData(order);
    }
}
