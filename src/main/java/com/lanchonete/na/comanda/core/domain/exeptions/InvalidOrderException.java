package com.lanchonete.na.comanda.core.domain.exeptions;

public class InvalidOrderException extends RuntimeException {
    
    public InvalidOrderException(String message) {
        super(message);
    }
}
