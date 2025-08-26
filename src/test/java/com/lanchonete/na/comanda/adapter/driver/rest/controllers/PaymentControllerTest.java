package com.lanchonete.na.comanda.adapter.driver.rest.controllers;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.REQUEST_TRACE_ID;
import static com.lanchonete.na.comanda.mocks.request.PaymentConfirmationRequestMock.paymentConfirmationRequestMock;
import static com.lanchonete.na.comanda.mocks.response.PaymentStatusResponseMock.paymentStatusResponseMock;
import static com.lanchonete.na.comanda.mocks.payment.PaymentStatusMock.paymentStatusMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.PaymentConfirmationRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.PaymentStatusResponse;
import com.lanchonete.na.comanda.core.application.usecases.payment.ConsultPaymentStatusUseCase;
import com.lanchonete.na.comanda.core.application.usecases.payment.ProcessPaymentWebhookUseCase;
import com.lanchonete.na.comanda.core.domain.payment.PaymentStatus;

public class PaymentControllerTest {
    
    @Mock
    private ConsultPaymentStatusUseCase consultPaymentStatusUseCase;

    @Mock
    private ProcessPaymentWebhookUseCase processPaymentWebhookUseCase;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void shouldReturnsOkWithPaymentStatusWhenConsultPaymentStatusAndOrderExists() {
        final PaymentStatus paymentStatus = paymentStatusMock();
        final PaymentStatusResponse paymentStatusResponse = paymentStatusResponseMock();

        when(consultPaymentStatusUseCase.getPaymentStatus(ORDER_ID)).thenReturn(paymentStatus);

        ResponseEntity<PaymentStatusResponse> response = paymentController.getPaymentStatus(REQUEST_TRACE_ID, ORDER_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentStatusResponse, response.getBody());
    }

    @Test
    void shouldReturnsOkWhenReceivePaymentConfirmationAndValidRequest() {
        final PaymentConfirmationRequest paymentConfirmationRequest = paymentConfirmationRequestMock();
  
        ResponseEntity<Void> response = paymentController.receivePaymentConfirmation(REQUEST_TRACE_ID, paymentConfirmationRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
