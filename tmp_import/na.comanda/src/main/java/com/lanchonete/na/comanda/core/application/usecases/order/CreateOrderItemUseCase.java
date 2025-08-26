package com.lanchonete.na.comanda.core.application.usecases.order;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;

public interface CreateOrderItemUseCase {
    OrderItem createOrderItem(Order order, OrderDTO.OrderItemDTO itemDTO);
}
