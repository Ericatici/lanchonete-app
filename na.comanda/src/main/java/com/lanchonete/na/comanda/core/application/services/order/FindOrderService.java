package com.lanchonete.na.comanda.core.application.services.order;

import static com.lanchonete.na.comanda.core.application.constants.ApiContants.ACTIVE_STATUSES;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.lanchonete.na.comanda.core.application.usecases.order.FindOrdersUseCase;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.OrderNotFoundException;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class FindOrderService implements FindOrdersUseCase{

    private final OrderRepository ordersRepository;

    @Override
    public List<Order> getAllOrdersByStatus(final OrderStatusEnum status) {
        log.info("Finding orders with Status: {}", status);

        final List<Order> orders = ordersRepository.findOrdersByStatus(status);

        if (CollectionUtils.isEmpty(orders)) {
            log.warn("No orders found with status: {}", status);
            throw new OrderNotFoundException("No orders found with status: " + status);
        }

        orders.sort(Comparator.comparing(Order::getId));

        return orders;
    }


    @Override
    public List<Order> getAllOrders() {
        log.info("Finding all orders");

        final List<Order> orders = ordersRepository.findAllOrders();

        if (CollectionUtils.isEmpty(orders)) {
            log.warn("No orders found");
            throw new OrderNotFoundException("No orders found");
        }

        orders.sort(Comparator.comparing(Order::getId));

        return orders;
    }


    @Override
    public List<Order> getAllOrdersByCustomer(final Customer customer) {
        log.info("Finding orders for Customer with cpf: {}", customer.getCpf());

        List<Order> orders = ordersRepository.findOrdersByCustomer(customer);

        if (CollectionUtils.isEmpty(orders)) {
            log.warn("No orders found for Customer with cpf: {}", customer.getCpf());
            throw new OrderNotFoundException("No orders found for Customer with cpf: " + customer.getCpf());
        }

        orders.sort(Comparator.comparing(Order::getId));

        return orders;
    }


    @Override
    public Order getOrderById(final Long id) {
        log.info("Finding order with id: {}", id);

        final Order order = ordersRepository.findOrderById(id);

        if (order == null) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        
        return order;
    }


    @Override
    public List<Order> getAllActiveOrdersSorted() {
        log.info("Finding all active orders, sorted by status and date.");

        List<Order> activeOrders = ordersRepository.findOrdersByStatusList(ACTIVE_STATUSES);

        if (CollectionUtils.isEmpty(activeOrders)) {
            log.warn("No active orders found.");
            return List.of(); 
        }

        return sortOrders(activeOrders);
    }

    public List<Order> sortOrders(List<Order> orders) {
        orders.sort(Comparator
            .comparing((Order order) -> {
                if (order.getStatus() == OrderStatusEnum.READY) {
                    return 1;
                } else if (order.getStatus() == OrderStatusEnum.IN_PREPARATION) {
                    return 2;
                } else if (order.getStatus() == OrderStatusEnum.RECEIVED) {
                    return 3;
                } else {
                    return 4; 
                }
            })
            .thenComparing(Order::getOrderDate)); 
        return orders;
    }
    
}
