package com.lanchonete.na.comanda.core.domain.exeptions;

public class InvalidPaymentException extends RuntimeException {

    public InvalidPaymentException(String message) {
        super(message);
    }
    
}
