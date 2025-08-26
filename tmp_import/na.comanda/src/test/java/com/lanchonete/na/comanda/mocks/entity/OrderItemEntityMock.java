package com.lanchonete.na.comanda.mocks.entity;

import static com.lanchonete.na.comanda.mocks.entity.ProductEntityMock.productEntityMock;

import java.math.BigDecimal;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderItemEntity;

public class OrderItemEntityMock {

    public static OrderItemEntity orderItemEntityMock() {
        return OrderItemEntity.builder()
                .product(productEntityMock())
                .quantity(2)
                .itemPrice(BigDecimal.valueOf(20))
            .build();
    }
    
}
