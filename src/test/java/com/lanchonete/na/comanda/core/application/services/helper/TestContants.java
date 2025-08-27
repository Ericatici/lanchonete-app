package com.lanchonete.na.comanda.core.application.services.helper;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestContants {

    public static final String CPF = "12345678901";
    public static final String CPF_2 = "98765432100";
    public static final String ITEM_ID = "1";
    public static final Long ORDER_ID = 1L;
    public static final String EMAIL = "test@example.com";
    public static final String INVALID_EMAIL = "invalid-email";
    public static final String UPDATED_EMAIL = "updated@example.com";
    public static final String NAME = "Test User";
    public static final String UPDATED_NAME = "Updated User";
    public static final String ITEM_NAME = "Burger";
    public static final String ITEM_DESCRIPTION = "Delicious";
    public static final BigDecimal ITEM_PRICE = BigDecimal.valueOf(10);
    public static final String REQUEST_TRACE_ID = "test-trace-id"; 
    public static final String PAYMENT_EXTERNAL_REFERENCE_ID = "1L";
    public static final BigDecimal TOTAL_PRICE = new BigDecimal("100.00");
    public static final String EXTERNAL_POS_ID = "SUC001";
}
