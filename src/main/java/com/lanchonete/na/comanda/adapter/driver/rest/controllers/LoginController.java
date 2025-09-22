package com.lanchonete.na.comanda.adapter.driver.rest.controllers;

import com.lanchonete.na.comanda.core.application.dto.LoginRequest;
import com.lanchonete.na.comanda.core.application.dto.LoginResponse;
import com.lanchonete.na.comanda.core.application.services.customer.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = loginService.execute(request);
        // Depending on the actual authentication mechanism, you might want to
        // return different HTTP statuses (e.g., 401 for authentication failure)
        // For now, we'll assume the LoginResponse itself indicates success/failure.
        return ResponseEntity.ok(response);
    }
}
