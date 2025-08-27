package com.lanchonete.na.comanda.adapter.driven.persistence.repositories;

import static com.lanchonete.na.comanda.mocks.order.OrderItemMock.orderItemMock;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.OrderItemEntityMapper.fromOrderItemToOrderItemEntity;
import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.OrderEntityMapper.fromOrderToOrderEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderItemEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.repositories.jpa.JpaOrderItemRepository;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;

class OrderItemRepositoryImplTest {
    
    @Mock
    private JpaOrderItemRepository jpaOrderItemRepository;

    @InjectMocks
    private OrderItemRepositoryImpl orderItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveAllOrderItems() {
        final OrderItem orderItem = orderItemMock();
        final Order order = orderMock();
       
        final OrderEntity orderEntity = fromOrderToOrderEntity(order);
        final OrderItemEntity orderItemEntity = fromOrderItemToOrderItemEntity(orderItem, orderEntity);
       
        when(jpaOrderItemRepository.save(any(OrderItemEntity.class))).thenReturn(orderItemEntity);

        OrderItem savedOrderItems = orderItemRepository.saveOrderItem(orderItem, order);

        assertNotNull(savedOrderItems);
        assertEquals(orderItem.getItemId(), savedOrderItems.getItemId());
        verify(jpaOrderItemRepository, times(1)).save(any(OrderItemEntity.class));
    }

}
