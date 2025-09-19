package com.lanchonete.na.comanda.core.application.services.customer;

import com.lanchonete.na.comanda.adapter.driven.auth.LambdaAuthGateway;
import com.lanchonete.na.comanda.core.application.dto.LoginRequest;
import com.lanchonete.na.comanda.core.application.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final LambdaAuthGateway lambdaAuthGateway;

    public LoginService(LambdaAuthGateway lambdaAuthGateway) {
        this.lambdaAuthGateway = lambdaAuthGateway;
    }

    public LoginResponse execute(LoginRequest request) {
        // Here you could add additional business logic before calling the Lambda
        // For example, logging, input validation, etc.
        return lambdaAuthGateway.authenticate(request);
    }
}
