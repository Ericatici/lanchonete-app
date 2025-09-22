package com.lanchonete.na.comanda.adapter.driven.rest.repositories.mercadopago;

import static com.lanchonete.na.comanda.core.application.constants.ApiContants.SLASH;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.TOTAL_PRICE;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.PAYMENT_EXTERNAL_REFERENCE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.lanchonete.na.comanda.adapter.driven.rest.request.MPAuthRequest;
import com.lanchonete.na.comanda.adapter.driven.rest.response.MPAuthResponse;
import com.lanchonete.na.comanda.adapter.driven.rest.response.MPPaymentConfirmationResponse;
import com.lanchonete.na.comanda.adapter.driven.rest.response.MPQrCodePaymentResponse;
import com.lanchonete.na.comanda.adapter.driven.rest.response.MPQrCodePaymentResponse.TypeResponse;
import com.lanchonete.na.comanda.core.domain.exeptions.MercadoPagoIntegrationException;


public class MercadoPagoGatewayRepositoryTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MercadoPagoGatewayRepository mercadoPagoGatewayRepository;
    
    private final String AUTH_URL = "http://localhost:8080/auth";
    private final String ORDERS_PATH = "/orders";
    private final String BASE_URL = "http://localhost:8080/payments";
    private final String CLIENT_ID = "test_client_id";
    private final String CLIENT_SECRET = "test_client_secret";
    private final String ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    private final Long EXPIRES_IN = 3600L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "authPath", AUTH_URL);
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "ordersPath", ORDERS_PATH);
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "mpUrl", BASE_URL);
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "clientId", CLIENT_ID);
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "secretId", CLIENT_SECRET);
    }

    @Test
    void shouldRefreshTokenSuccessfully() {
        MPAuthResponse authResponse = new MPAuthResponse();
        authResponse.setAccessToken(ACCESS_TOKEN);
        authResponse.setExpiresIn(EXPIRES_IN);

        when(restTemplate.postForEntity(
                eq(BASE_URL + AUTH_URL),
                any(MPAuthRequest.class),
                eq(MPAuthResponse.class)
        )).thenReturn(new ResponseEntity<>(authResponse, HttpStatus.OK));

        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "accessToken", null);
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "tokenExpiryTime", null);

        ReflectionTestUtils.invokeMethod(mercadoPagoGatewayRepository, "refreshToken");

        String currentAccessToken = (String) ReflectionTestUtils.getField(mercadoPagoGatewayRepository, "accessToken");
        LocalDateTime tokenExpiryTime = (LocalDateTime) ReflectionTestUtils.getField(mercadoPagoGatewayRepository, "tokenExpiryTime");

        assertEquals(ACCESS_TOKEN, currentAccessToken);
        assertNotNull(tokenExpiryTime);
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenFailsDueToNullResponse() {
        MPAuthResponse response = null;
        when(restTemplate.exchange(
                eq(AUTH_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(MPAuthResponse.class)
        )).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "accessToken", null);
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "tokenExpiryTime", null);

        assertThrows(MercadoPagoIntegrationException.class, () ->
                ReflectionTestUtils.invokeMethod(mercadoPagoGatewayRepository, "refreshToken")
        );

    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenFailsDueToEmptyAccessToken() {
        MPAuthResponse authResponse = new MPAuthResponse();
        authResponse.setAccessToken(null);
        authResponse.setExpiresIn(EXPIRES_IN);

        when(restTemplate.exchange(
                eq(AUTH_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(MPAuthResponse.class)
        )).thenReturn(new ResponseEntity<>(authResponse, HttpStatus.OK));

        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "accessToken", null);
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "tokenExpiryTime", null);

        assertThrows(MercadoPagoIntegrationException.class, () ->
                ReflectionTestUtils.invokeMethod(mercadoPagoGatewayRepository, "refreshToken")
        );
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenEncountersHttpError() {
        when(restTemplate.exchange(
                eq(AUTH_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(MPAuthResponse.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "accessToken", null);
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "tokenExpiryTime", null);

        MercadoPagoIntegrationException exception = assertThrows(MercadoPagoIntegrationException.class, () ->
                ReflectionTestUtils.invokeMethod(mercadoPagoGatewayRepository, "refreshToken")
        );
        assertEquals("Error trying to get auth token from Mercado Pago.", exception.getMessage());
    }

    @Test
    void shouldCreateQrCodeForPaymentSuccessfully() {
        MPQrCodePaymentResponse qrCodeResponse = MPQrCodePaymentResponse.builder()
                .id(PAYMENT_EXTERNAL_REFERENCE_ID)
                .status("approved")
                .externalReference(ORDER_ID.toString())
                .typeResponse(TypeResponse.builder().qrData("qrCodeData").build())
                .build();

        when(restTemplate.postForEntity(
                eq(BASE_URL + ORDERS_PATH),
                any(HttpEntity.class),
                eq(MPQrCodePaymentResponse.class)
        )).thenReturn(new ResponseEntity<>(qrCodeResponse, HttpStatus.CREATED));

        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "accessToken", ACCESS_TOKEN);
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "tokenExpiryTime", LocalDateTime.now().plusMinutes(10));

        MPQrCodePaymentResponse response = mercadoPagoGatewayRepository.createQrCodeForPayment(ORDER_ID, TOTAL_PRICE);

        assertNotNull(response);
        assertEquals(PAYMENT_EXTERNAL_REFERENCE_ID, response.getId());
        assertEquals("qrCodeData", response.getTypeResponse().getQrData());
    }

    @Test
    void shouldGetPaymentConfirmationSuccessfully() {
        MPPaymentConfirmationResponse paymentConfirmationResponse = new MPPaymentConfirmationResponse();
        paymentConfirmationResponse.setId(PAYMENT_EXTERNAL_REFERENCE_ID);
        paymentConfirmationResponse.setStatus("approved");
        paymentConfirmationResponse.setTotalAmount(TOTAL_PRICE.doubleValue());

        when(restTemplate.exchange(
                eq(BASE_URL + ORDERS_PATH + SLASH + PAYMENT_EXTERNAL_REFERENCE_ID),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(MPPaymentConfirmationResponse.class)
        )).thenReturn(new ResponseEntity<>(paymentConfirmationResponse, HttpStatus.OK));

        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "accessToken", ACCESS_TOKEN);
        ReflectionTestUtils.setField(mercadoPagoGatewayRepository, "tokenExpiryTime", LocalDateTime.now().plusMinutes(10));

        MPPaymentConfirmationResponse response = mercadoPagoGatewayRepository.getPaymentConfirmation(PAYMENT_EXTERNAL_REFERENCE_ID);

        assertNotNull(response);
        assertEquals(PAYMENT_EXTERNAL_REFERENCE_ID, response.getId());
        assertEquals("approved", response.getStatus());
        assertEquals(TOTAL_PRICE.doubleValue(), response.getTotalAmount());
    }

}
