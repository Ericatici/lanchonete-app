package com.lanchonete.na.comanda.adapter.driven.rest.repositories;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.PAYMENT_EXTERNAL_REFERENCE_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.TOTAL_PRICE;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.adapter.driven.rest.repositories.mercadopago.MercadoPagoGatewayRepository;
import com.lanchonete.na.comanda.adapter.driven.rest.response.MPPaymentConfirmationResponse;
import com.lanchonete.na.comanda.adapter.driven.rest.response.MPQrCodePaymentResponse;
import com.lanchonete.na.comanda.adapter.driven.rest.response.MPQrCodePaymentResponse.TypeResponse;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.payment.PaymentConfirmation;
import com.lanchonete.na.comanda.core.domain.payment.PaymentData;

public class PaymentRepositoryImplTest {
    
    @Mock
    private MercadoPagoGatewayRepository mercadoPagoGateway;

    @InjectMocks
    private PaymentRepositoryImpl paymentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPaymentConfirmationWhenGetPaymentStatusSuccessfully() {
        MPPaymentConfirmationResponse mpResponse = new MPPaymentConfirmationResponse();
        mpResponse.setId(PAYMENT_EXTERNAL_REFERENCE_ID);
        mpResponse.setStatus("approved");
        mpResponse.setTotalAmount(TOTAL_PRICE.doubleValue());

        when(mercadoPagoGateway.getPaymentConfirmation(anyString())).thenReturn(mpResponse);

        PaymentConfirmation result = paymentRepository.getPaymentStatus(PAYMENT_EXTERNAL_REFERENCE_ID);

        assertNotNull(result);
        assertEquals(PAYMENT_EXTERNAL_REFERENCE_ID, result.getId());
        assertEquals("approved", result.getStatus());
        assertEquals(TOTAL_PRICE.doubleValue(), result.getTotalAmount());
    }

    @Test
    void shouldReturnNullWhenGetPaymentStatusReceivesNullResponse() {
        when(mercadoPagoGateway.getPaymentConfirmation(anyString())).thenReturn(null);

        PaymentConfirmation result = paymentRepository.getPaymentStatus(PAYMENT_EXTERNAL_REFERENCE_ID);

        assertNull(result);
    }

    @Test
    void shouldReturnPaymentDataWhenGetPaymentDataSuccessfully() {
        Order order = orderMock();
        MPQrCodePaymentResponse mpResponse = MPQrCodePaymentResponse.builder()
                .id(PAYMENT_EXTERNAL_REFERENCE_ID)
                .status("approved")
                .externalReference(ORDER_ID.toString())
                .typeResponse(TypeResponse.builder().qrData("qrCodeDataExample").build())
                .build();

        when(mercadoPagoGateway.createQrCodeForPayment(anyLong(), any())).thenReturn(mpResponse);

        PaymentData result = paymentRepository.getPaymentData(order);

        assertNotNull(result);
        assertEquals(PAYMENT_EXTERNAL_REFERENCE_ID, result.getPaymentId());
        assertEquals("qrCodeDataExample", result.getQrCode());
    }

    @Test
    void shouldReturnNullWhenGetPaymentDataReceivesNullResponse() {
        Order order = orderMock();
        when(mercadoPagoGateway.createQrCodeForPayment(anyLong(), any())).thenReturn(null);

        PaymentData result = paymentRepository.getPaymentData(order);

        assertNull(result);
    }
}
