package com.lanchonete.na.comanda.mocks.response;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.mocks.response.OrderItemResponseMock.orderItemResponseMock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.OrderResponse;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.OrderResponse.OrderItemResponse;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;

public class OrderResponseMock {
    
    public static OrderResponse orderResponseMock() {
        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
        orderItemResponseList.add(orderItemResponseMock());

        return OrderResponse.builder()
                .id(ORDER_ID)
                .customerCpf(CPF)
                .status(OrderStatusEnum.IN_PREPARATION)
                .items(orderItemResponseList)
                .totalPrice(BigDecimal.valueOf(10))
            .build();
    }

    public static OrderResponse orderResponseMock(final OrderStatusEnum orderStatus) {
        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
        orderItemResponseList.add(orderItemResponseMock());

        return OrderResponse.builder()
                .id(ORDER_ID)
                .customerCpf(CPF)
                .status(orderStatus)
                .items(orderItemResponseList)
                .totalPrice(BigDecimal.valueOf(10))
            .build();
    }

}
