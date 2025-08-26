package com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers;

import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.CustomerEntityMapper.fromCustomerToCustomerEntity;
import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.OrderItemEntityMapper.fromOrderItemToOrderItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.CustomerEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderItemEntity;
import com.lanchonete.na.comanda.core.domain.order.Order;

public class OrderEntityMapper {

    public static OrderEntity fromOrderToOrderEntity (final Order order) {   
        final CustomerEntity customerEntity = order.getCustomer() != null ? fromCustomerToCustomerEntity(order.getCustomer()) : null;
        OrderEntity orderEntity = OrderEntity.builder()
                                    .id(order.getId())
                                    .customer(customerEntity)
                                    .orderDate(order.getOrderDate())
                                    .status(order.getStatus())
                                    .paymentId(order.getPaymentId())
                                    .qrCodeData(order.getQrCodeData())
                                    .paymentStatus(order.getPaymentStatus())
                                    .totalPrice(order.getTotalPrice())
                                    .build();

        final List<OrderItemEntity> orderItems = order.getItems().stream()
                .map(item -> fromOrderItemToOrderItemEntity(item, orderEntity))
                .collect(Collectors.toCollection(ArrayList::new));  

        orderEntity.setItems(orderItems);


        if (order.getCreatedDate() != null) {
            orderEntity.setCreatedDate(order.getCreatedDate());
        }

        if (order.getUpdatedDate() != null) {
            orderEntity.setUpdatedDate(order.getUpdatedDate());
        }

        return orderEntity;
    }
    
}
