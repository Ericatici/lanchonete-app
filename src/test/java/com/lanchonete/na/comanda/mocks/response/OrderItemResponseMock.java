package com.lanchonete.na.comanda.mocks.response;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;

import java.math.BigDecimal;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.OrderResponse.OrderItemResponse;

public class OrderItemResponseMock {

    public static OrderItemResponse orderItemResponseMock() {
        return OrderItemResponse.builder()
                .productId(ITEM_ID)
                .quantity(2)
                .itemPrice(BigDecimal.valueOf(20))
            .build();
    }
    
}
