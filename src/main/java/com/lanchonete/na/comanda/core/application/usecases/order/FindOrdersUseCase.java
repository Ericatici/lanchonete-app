package com.lanchonete.na.comanda.core.application.usecases.order;

import java.util.List;

import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;

public interface FindOrdersUseCase {
    List<Order> getAllOrdersByStatus(OrderStatusEnum status);
    List<Order> getAllOrders();
    List<Order> getAllOrdersByCustomer(Customer customer);
    Order getOrderById(Long id);
    List<Order> getAllActiveOrdersSorted();
}
