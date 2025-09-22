package com.lanchonete.na.comanda.adapter.driven.auth;

import com.lanchonete.na.comanda.core.application.dto.LoginRequest;
import com.lanchonete.na.comanda.core.application.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LambdaAuthGateway {

    @Value("${lambda.auth.api.url}")
    private String lambdaAuthApiUrl;

    private final RestTemplate restTemplate;

    public LambdaAuthGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LoginResponse authenticate(LoginRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(lambdaAuthApiUrl + "/login", entity, LoginResponse.class);
    }
}
