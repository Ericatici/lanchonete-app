package com.lanchonete.na.comanda.adapter.driven.persistence.repositories;

import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.OrderItemEntityMapper.fromOrderItemToOrderItemEntity;

import org.springframework.stereotype.Component;

import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.OrderEntityMapper.fromOrderToOrderEntity;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderItemEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.repositories.jpa.JpaOrderItemRepository;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;
import com.lanchonete.na.comanda.core.domain.repositories.OrderItemRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JpaOrderItemRepository orderItemRepository;

    @Override
    public OrderItem saveOrderItem(final OrderItem orderItem, final Order order) {
        final OrderEntity orderEntity = fromOrderToOrderEntity(order);
        final OrderItemEntity orderItemEntity = fromOrderItemToOrderItemEntity(orderItem, orderEntity);

        final OrderItemEntity savedOrderItemEntity = orderItemRepository.save(orderItemEntity);
        
        return savedOrderItemEntity.toOrderItem();
    }
    
}
