package com.lanchonete.na.comanda.mocks.request;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.mocks.request.OrderItemRequestMock.orderItemRequestMock;

import java.util.Collections;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.OrderRequest;

public class OrderRequestMock {

    public static OrderRequest orderRequestMock() {
        return OrderRequest.builder()
                .customerCpf(CPF)
                .items(Collections.singletonList(orderItemRequestMock()))
            .build();
    }
    
}
