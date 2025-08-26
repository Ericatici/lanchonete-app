package com.lanchonete.na.comanda.mocks.dto;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO.OrderItemDTO;

public class OrderItemDTOMock {

    public static OrderItemDTO orderItemDTOMock() {
        return OrderItemDTO.builder()
                .productId(ITEM_ID)
                .quantity(2)
            .build();
    }
    
}
