package com.lanchonete.na.comanda.core.domain.exeptions;

public class MercadoPagoIntegrationException extends RuntimeException {

    public MercadoPagoIntegrationException(String message) {
        super(message);
    }

    public MercadoPagoIntegrationException(String message, Exception e) {
        super(message, e);
    }
    
}
