package com.lanchonete.na.comanda.mocks.request;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.OrderRequest.OrderItemRequest;

public class OrderItemRequestMock {

     public static OrderItemRequest orderItemRequestMock() {
        return OrderItemRequest.builder()
                .productId(ITEM_ID)
                .quantity(2)
            .build();
    }
    
}
