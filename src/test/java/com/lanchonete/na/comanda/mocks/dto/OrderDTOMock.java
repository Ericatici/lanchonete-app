package com.lanchonete.na.comanda.mocks.dto;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.mocks.dto.OrderItemDTOMock.orderItemDTOMock;

import java.util.Collections;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO;

public class OrderDTOMock {

    public static OrderDTO orderDTOMock() {
        return OrderDTO.builder()
                .customerCpf(CPF)
                .items(Collections.singletonList(orderItemDTOMock()))
            .build();
    }
}
