package com.lanchonete.na.comanda.mocks.order;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static com.lanchonete.na.comanda.mocks.order.OrderItemMock.orderItemMock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;

public class OrderMock {

    public static Order orderMock() {
        return orderMock(OrderStatusEnum.IN_PREPARATION);
    }

    public static Order orderMock(final OrderStatusEnum orderStatus) {
        List<OrderItem> orderItemEntityList = new ArrayList<>();
        orderItemEntityList.add(orderItemMock());

        return Order.builder()
                .id(ORDER_ID)
                .customer(customerMock())
                .status(orderStatus)
                .items(orderItemEntityList)
                .totalPrice(BigDecimal.valueOf(10))
            .build();
    }
    
}
