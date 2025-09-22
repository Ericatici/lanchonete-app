package com.lanchonete.na.comanda.core.domain.repositories;

import java.util.List;

import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;

public interface OrderRepository {

    Order saveOrder(Order order);
    List<Order> findOrdersByStatus(OrderStatusEnum status);
    List<Order> findAllOrders();
    List<Order> findOrdersByCustomer(Customer customer);
    Order findOrderById(Long orderId);
    Order updateOrder(Order order);
    Order findOrderByPaymentId(String paymentId);
    List<Order> findOrdersByStatusList(List<OrderStatusEnum> statuses);

    
}
