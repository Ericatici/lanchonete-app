package com.lanchonete.na.comanda.core.application.services.payment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.application.dto.PaymentConfirmationDTO;
import com.lanchonete.na.comanda.core.application.dto.PaymentConfirmationDTO.PaymentConfirmationDataDTO;
import com.lanchonete.na.comanda.core.application.usecases.order.UpdateOrderPaymentStatusUseCase;
import com.lanchonete.na.comanda.core.domain.exeptions.InvalidPaymentException;
import com.lanchonete.na.comanda.core.domain.exeptions.OrderNotFoundException;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;
import com.lanchonete.na.comanda.core.domain.repositories.PaymentRepository;

public class ProcessPaymentWebhookServiceTest {

   @Mock
    private OrderRepository ordersRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UpdateOrderPaymentStatusUseCase updateOrderPaymentStatusUseCase;

    @InjectMocks
    private ProcessPaymentWebhookService processPaymentWebhookService;

    private PaymentConfirmationDTO paymentConfirmationDTO;
    private Order order;
    private PaymentConfirmation paymentConfirmation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        paymentConfirmationDTO = PaymentConfirmationDTO.builder()
        .data(PaymentConfirmationDataDTO.builder().id("payment-id-123").build())
        .build();

        order = Order.builder()
            .id(1L)
            .paymentId("payment-id-123")
            .build();
        
        paymentConfirmation = PaymentConfirmation.builder()
            .status("processed")
            .id("payment-id-123")
            .build();
    }

    @Test
    void shouldProcessPaymentConfirmationSuccessfully() {
        when(ordersRepository.findOrderByPaymentId("payment-id-123")).thenReturn(order);
        when(paymentRepository.getPaymentStatus("payment-id-123")).thenReturn(paymentConfirmation);

        assertDoesNotThrow(() -> processPaymentWebhookService.processPaymentConfirmation(paymentConfirmationDTO));

        verify(ordersRepository, times(1)).findOrderByPaymentId("payment-id-123");
        verify(paymentRepository, times(1)).getPaymentStatus("payment-id-123");
        verify(updateOrderPaymentStatusUseCase, times(1)).updateOrderPaymentStatus(order, paymentConfirmation);
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderNotFound() {
        when(ordersRepository.findOrderByPaymentId("payment-id-123")).thenReturn(null);

        OrderNotFoundException thrown = assertThrows(OrderNotFoundException.class, () ->
                processPaymentWebhookService.processPaymentConfirmation(paymentConfirmationDTO));

        assertEquals("Order with paymentId payment-id-123 not found", thrown.getMessage());
        verify(ordersRepository, times(1)).findOrderByPaymentId("payment-id-123");
        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(updateOrderPaymentStatusUseCase);
    }

    @Test
    void shouldThrowInvalidPaymentExceptionWhenPaymentConfirmationIsNull() {
        when(ordersRepository.findOrderByPaymentId("payment-id-123")).thenReturn(order);
        when(paymentRepository.getPaymentStatus("payment-id-123")).thenReturn(null);

        InvalidPaymentException thrown = assertThrows(InvalidPaymentException.class, () ->
                processPaymentWebhookService.processPaymentConfirmation(paymentConfirmationDTO));

        assertEquals("No payment confirmation received for paymentId payment-id-123", thrown.getMessage());
        verify(ordersRepository, times(1)).findOrderByPaymentId("payment-id-123");
        verify(paymentRepository, times(1)).getPaymentStatus("payment-id-123");
        verifyNoInteractions(updateOrderPaymentStatusUseCase);
    }

   
}
