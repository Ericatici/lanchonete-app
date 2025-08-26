package com.lanchonete.na.comanda.core.domain.repositories;

import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;

public interface OrderItemRepository {

    OrderItem saveOrderItem (OrderItem orderItem, Order order);
    
}
