package com.lanchonete.na.comanda.core.domain.exeptions;

public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(String message) {
        super(message);
    }

}
