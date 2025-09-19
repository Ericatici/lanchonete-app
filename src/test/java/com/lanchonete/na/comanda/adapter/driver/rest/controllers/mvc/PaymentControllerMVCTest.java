package com.lanchonete.na.comanda.adapter.driver.rest.controllers.mvc;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.mocks.request.PaymentConfirmationRequestMock.paymentConfirmationRequestMock;
import static com.lanchonete.na.comanda.mocks.payment.PaymentStatusMock.paymentStatusMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.PaymentController;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.PaymentConfirmationRequest;
import com.lanchonete.na.comanda.core.application.usecases.payment.ConsultPaymentStatusUseCase;
import com.lanchonete.na.comanda.core.application.usecases.payment.ProcessPaymentWebhookUseCase;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;

@WebMvcTest(PaymentController.class)
public class PaymentControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ConsultPaymentStatusUseCase consultPaymentStatusUseCase;

    @MockitoBean
    private ProcessPaymentWebhookUseCase processPaymentWebhookUseCase;

    @Test
    void shouldGetPaymentStatusSuccessfullyWhenOrderExists() throws Exception {
        when(consultPaymentStatusUseCase.getPaymentStatus(anyLong())).thenReturn(paymentStatusMock());

        mockMvc.perform(MockMvcRequestBuilders.get("/" + ORDER_ID + "/payment-status"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(ORDER_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentStatus").value(PaymentStatusEnum.APPROVED.name()));
    }

    @Test
    void shouldReceivePaymentConfirmationSuccessfullyWhenInputIsValid() throws Exception {
        PaymentConfirmationRequest request = paymentConfirmationRequestMock();
        doNothing().when(processPaymentWebhookUseCase).processPaymentConfirmation(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/webhooks/payment-confirmation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
