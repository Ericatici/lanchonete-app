package com.lanchonete.na.comanda.core.domain.exeptions;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
