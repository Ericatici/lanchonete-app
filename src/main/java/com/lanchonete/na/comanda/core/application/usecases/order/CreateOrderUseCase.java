package com.lanchonete.na.comanda.core.application.usecases.order;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.domain.order.Order;

public interface CreateOrderUseCase {
    Order createOrder(OrderDTO order);
}
