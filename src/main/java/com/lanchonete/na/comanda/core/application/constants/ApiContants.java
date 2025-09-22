package com.lanchonete.na.comanda.core.application.constants;

import java.util.List;

import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiContants {

    public static final String REQUEST_TRACE_ID = "requestTraceId";
    public static final List<OrderStatusEnum> ACTIVE_STATUSES = List.of(OrderStatusEnum.RECEIVED, OrderStatusEnum.IN_PREPARATION, OrderStatusEnum.READY);
    public static final String ACESS_TOKEN_PARAM = "access_token";
    public static final String SLASH = "/";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String X_IDEMPOTENCY_KEY = "X-Idempotency-Key";
    public static final String DYNAMIC = "dynamic";
    public static final String QR = "qr"; 
    public static final String ORDER_DESCRIPTION = "Order payment: ";
    public static final String PROCESSED_STATUS = "processed";
    public static final String EXPIRED_STATUS = "expired";
    public static final String REFUNDED_STATUS = "refunded";
    public static final String CANCELED_STATUS = "canceled";

    
}
