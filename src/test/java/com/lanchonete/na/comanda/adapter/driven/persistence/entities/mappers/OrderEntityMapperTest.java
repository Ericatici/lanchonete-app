package com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers;

import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static com.lanchonete.na.comanda.mocks.order.OrderItemMock.orderItemMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderEntity;
import com.lanchonete.na.comanda.core.domain.order.Order;

public class OrderEntityMapperTest {
    
    
    @Test
    void shouldMapOrderToOrderEntityCorrectly() {
        Order order = orderMock();
        order.setCustomer(customerMock());
        order.setItems(Collections.singletonList(orderItemMock()));
        order.setCreatedDate(Instant.now());
        order.setUpdatedDate(Instant.now());

        OrderEntity orderEntity = OrderEntityMapper.fromOrderToOrderEntity(order);

        assertNotNull(orderEntity);
        assertEquals(order.getId(), orderEntity.getId());
        assertEquals(order.getCustomer().getCpf(), orderEntity.getCustomer().getCpf());
        assertEquals(order.getOrderDate(), orderEntity.getOrderDate());
        assertEquals(order.getStatus(), orderEntity.getStatus());
        assertEquals(order.getTotalPrice(), orderEntity.getTotalPrice());
        assertEquals(order.getCreatedDate(), orderEntity.getCreatedDate());
        assertEquals(order.getUpdatedDate(), orderEntity.getUpdatedDate());
        assertNotNull(orderEntity.getItems());
        assertEquals(1, orderEntity.getItems().size());
        assertEquals(order.getItems().get(0).getProduct().getItemId(), orderEntity.getItems().get(0).getProduct().getItemId());
    }

    @Test
    void shouldMapOrderToOrderEntityWhenCustomerIsNull() {
        Order order = orderMock();
        order.setCustomer(null);
        order.setItems(Collections.singletonList(orderItemMock()));

        OrderEntity orderEntity = OrderEntityMapper.fromOrderToOrderEntity(order);

        assertNotNull(orderEntity);
        assertNull(orderEntity.getCustomer());
    }

    @Test
    void shouldMapOrderToOrderEntityWhenItemsAreEmpty() {
        Order order = orderMock();
        order.setItems(Collections.emptyList());

        OrderEntity orderEntity = OrderEntityMapper.fromOrderToOrderEntity(order);

        assertNotNull(orderEntity);
        assertNotNull(orderEntity.getItems());
        assertEquals(0, orderEntity.getItems().size());
    }

    @Test
    void shouldMapOrderToOrderEntityWhenDatesAreNull() {
        Order order = orderMock();
        order.setCreatedDate(null);
        order.setUpdatedDate(null);

        OrderEntity orderEntity = OrderEntityMapper.fromOrderToOrderEntity(order);

        assertNotNull(orderEntity);
        assertNull(orderEntity.getCreatedDate());
        assertNull(orderEntity.getUpdatedDate());
    }

}
