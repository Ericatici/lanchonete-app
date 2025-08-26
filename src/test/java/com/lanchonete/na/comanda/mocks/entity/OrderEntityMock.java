package com.lanchonete.na.comanda.mocks.entity;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.mocks.entity.CustomerEntityMock.customerEntityMock;
import static com.lanchonete.na.comanda.mocks.entity.OrderItemEntityMock.orderItemEntityMock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderItemEntity;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;;

public class OrderEntityMock {

    public static OrderEntity orderEntityMock() {
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();
        orderItemEntityList.add(orderItemEntityMock());

        return OrderEntity.builder()
                .id(ORDER_ID)
                .customer(customerEntityMock())
                .status(OrderStatusEnum.IN_PREPARATION)
                .items(orderItemEntityList)
                .totalPrice(BigDecimal.valueOf(10))
            .build();
    }
    
}
