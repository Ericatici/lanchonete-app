package com.lanchonete.na.comanda.core.domain.exeptions;

public class ProductDeletionException extends RuntimeException  {
    
    public ProductDeletionException(String message) {
        super(message);
    }

    public ProductDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

}
