package com.lanchonete.na.comanda.core.application.usecases.order;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;

public interface UpdateOrderUseCase {
    Order updateOrder(Long orderId, OrderDTO order);
    Order updateOrder(Order order);
    Order updateOrderStatusById(Long orderId, OrderStatusEnum status);
}
