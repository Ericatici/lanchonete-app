package com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers;

import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.ProductEntityMapper.fromProductToProductEntity;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderEntity;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderItemEntity;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;

public class OrderItemEntityMapper {
    
    public static OrderItemEntity fromOrderItemToOrderItemEntity(final OrderItem orderItem, final OrderEntity order){
        OrderItemEntity orderItemEntity = OrderItemEntity.builder()
                                            .id(orderItem.getId())
                                            .order(order)
                                            .product(fromProductToProductEntity(orderItem.getProduct()))
                                            .quantity(orderItem.getQuantity())
                                            .itemPrice(orderItem.getItemPrice())
                                            .build();

        if (orderItem.getCreatedDate() != null) {
            orderItemEntity.setCreatedDate(orderItem.getCreatedDate());
        }

        if (orderItem.getUpdatedDate() != null) {
            orderItemEntity.setUpdatedDate(orderItem.getUpdatedDate());
        }
        
        return orderItemEntity;
    }  
}
