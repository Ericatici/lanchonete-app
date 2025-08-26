package com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers;

import static com.lanchonete.na.comanda.mocks.order.OrderItemMock.orderItemMock;
import static com.lanchonete.na.comanda.mocks.order.OrderItemMock.orderItemWithDatesMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderItemEntity;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;

class OrderItemEntityMapperTest {
    
    @Test
    void shouldMapOrderItemToOrderItemEntity() {
        final OrderItem orderItem = orderItemMock();

        final OrderItemEntity orderItemEntity = OrderItemEntityMapper.fromOrderItemToOrderItemEntity(orderItem, new OrderEntity());


        assertNotNull(orderItemEntity);
        assertEquals(orderItem.getItemId(), orderItemEntity.getProduct().getItemId());
        assertEquals(orderItem.getQuantity(), orderItemEntity.getQuantity());
        assertEquals(orderItem.getItemPrice(), orderItemEntity.getItemPrice());
        assertNotNull(orderItemEntity.getProduct()); 
        assertEquals(orderItem.getProduct().getItemId(), orderItemEntity.getProduct().getItemId());
        assertNull(orderItemEntity.getCreatedDate());
        assertNull(orderItemEntity.getUpdatedDate());
    }

    @Test
    void shouldMapOrderItemToOrderItemEntityWithDates() {
        final OrderItem orderItem = orderItemWithDatesMock(); 

        final OrderItemEntity orderItemEntity = OrderItemEntityMapper.fromOrderItemToOrderItemEntity(orderItem, new OrderEntity());

        assertNotNull(orderItemEntity);
        assertEquals(orderItem.getItemId(), orderItemEntity.getProduct().getItemId());
        assertEquals(orderItem.getQuantity(), orderItemEntity.getQuantity());
        assertEquals(orderItem.getItemPrice(), orderItemEntity.getItemPrice());
        assertNotNull(orderItemEntity.getProduct()); 
        assertEquals(orderItem.getProduct().getItemId(), orderItemEntity.getProduct().getItemId());
        assertEquals(orderItem.getCreatedDate(), orderItemEntity.getCreatedDate());
        assertEquals(orderItem.getUpdatedDate(), orderItemEntity.getUpdatedDate());
    }
}
