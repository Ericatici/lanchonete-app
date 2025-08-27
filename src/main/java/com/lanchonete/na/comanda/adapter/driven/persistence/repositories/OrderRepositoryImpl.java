package com.lanchonete.na.comanda.adapter.driven.persistence.repositories;

import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.CustomerEntityMapper.fromCustomerToCustomerEntity;
import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.OrderEntityMapper.fromOrderToOrderEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.CustomerEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.repositories.jpa.JpaOrdersRepository;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrdersRepository ordersRepository;

    @Override
    @Transactional
    public Order saveOrder(final Order order) {
        final OrderEntity orderEntity = fromOrderToOrderEntity(order);

        final OrderEntity savedOrderEntity = ordersRepository.save(orderEntity);

        return savedOrderEntity.toOrder();

    }

    @Override
    public List<Order> findOrdersByStatus(OrderStatusEnum status) {
        final List<OrderEntity> orders = ordersRepository.findAllByStatus(status);

        return orders.stream()
                .map(OrderEntity::toOrder)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Order> findAllOrders() {
        final List<OrderEntity> orders = ordersRepository.findAll();

        return orders.stream()
                .map(OrderEntity::toOrder)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Order> findOrdersByCustomer(Customer customer) {
        final CustomerEntity customerEntity = fromCustomerToCustomerEntity(customer);
        
        final List<OrderEntity> orders = ordersRepository.findAllByCustomer(customerEntity);

        return orders.stream()
                .map(OrderEntity::toOrder)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Order findOrderById(Long orderId) {
        final Optional<OrderEntity> orderOptional = ordersRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            return orderOptional.get().toOrder();
        }

        return null;
    }

    @Override
    @Transactional
    public Order updateOrder(Order order) {
        final OrderEntity orderEntity = fromOrderToOrderEntity(order);

        final OrderEntity updatedProduct = ordersRepository.save(orderEntity);

        return updatedProduct.toOrder();
    }

    @Override
    public Order findOrderByPaymentId(String paymentId) {
        final Optional<OrderEntity> orderOptional = ordersRepository.findByPaymentId(paymentId);

        if (orderOptional.isPresent()) {
            return orderOptional.get().toOrder();
        }

        return null;
    }

    @Override
    public List<Order> findOrdersByStatusList(List<OrderStatusEnum> statuses) {
        final List<OrderEntity> orders = ordersRepository.findAllByStatusIn(statuses);

        return orders.stream()
                .map(OrderEntity::toOrder)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
}
