package com.lanchonete.na.comanda.mocks.order;

import static com.lanchonete.na.comanda.mocks.product.ProductMock.productMock;

import java.math.BigDecimal;
import java.time.Instant;

import com.lanchonete.na.comanda.core.domain.order.OrderItem;

public class OrderItemMock {

    public static OrderItem orderItemMock() {
        return OrderItem.builder()
                .product(productMock())
                .quantity(2)
                .itemPrice(BigDecimal.valueOf(20))
            .build();
    }

    public static OrderItem orderItemWithDatesMock() {
        return OrderItem.builder()
                .product(productMock())
                .quantity(2)
                .itemPrice(BigDecimal.valueOf(20))
                .createdDate(Instant.now())
                .updatedDate(Instant.now())
            .build();
    }
    
}
