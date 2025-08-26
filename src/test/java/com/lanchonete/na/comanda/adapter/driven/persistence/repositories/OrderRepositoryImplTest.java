package com.lanchonete.na.comanda.adapter.driven.persistence.repositories;

import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.OrderEntityMapper.fromOrderToOrderEntity;
import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.CustomerEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.repositories.jpa.JpaOrdersRepository;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;

class OrderRepositoryImplTest {
    
    @InjectMocks
    private OrderRepositoryImpl orderRepository;

    @Mock
    private JpaOrdersRepository jpaOrdersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveOrderSuccessfully() {
        Order order = orderMock();
        OrderEntity orderEntity = fromOrderToOrderEntity(order);
        when(jpaOrdersRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        Order savedOrder = orderRepository.saveOrder(order);

        assertNotNull(savedOrder);
        assertEquals(order.getId(), savedOrder.getId());
        verify(jpaOrdersRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void shouldFindOrdersByStatus() {
        OrderStatusEnum status = OrderStatusEnum.IN_PREPARATION;
        OrderEntity orderEntity = fromOrderToOrderEntity(orderMock());
        when(jpaOrdersRepository.findAllByStatus(status)).thenReturn(Arrays.asList(orderEntity));

        List<Order> orders = orderRepository.findOrdersByStatus(status);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(status, orders.get(0).getStatus());
        verify(jpaOrdersRepository, times(1)).findAllByStatus(status);
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersFoundByStatus() {
        OrderStatusEnum status = OrderStatusEnum.READY;
        when(jpaOrdersRepository.findAllByStatus(status)).thenReturn(Collections.emptyList());

        List<Order> orders = orderRepository.findOrdersByStatus(status);

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
        verify(jpaOrdersRepository, times(1)).findAllByStatus(status);
    }

    @Test
    void shouldFindAllOrders() {
        OrderEntity orderEntity1 = fromOrderToOrderEntity(orderMock());
        Order order2 = orderMock();
        order2.setId(2L);
        OrderEntity orderEntity2 = fromOrderToOrderEntity(order2);
        when(jpaOrdersRepository.findAll()).thenReturn(Arrays.asList(orderEntity1, orderEntity2));

        List<Order> orders = orderRepository.findAllOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(jpaOrdersRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersFound() {
        when(jpaOrdersRepository.findAll()).thenReturn(Collections.emptyList());

        List<Order> orders = orderRepository.findAllOrders();

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
        verify(jpaOrdersRepository, times(1)).findAll();
    }

    @Test
    void shouldFindOrdersByCustomer() {
        Customer customer = customerMock();
        Order order = orderMock();
        order.setCustomer(customer);
        OrderEntity orderEntity = fromOrderToOrderEntity(order);
        when(jpaOrdersRepository.findAllByCustomer(any(CustomerEntity.class))).thenReturn(Arrays.asList(orderEntity));

        List<Order> orders = orderRepository.findOrdersByCustomer(customer);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(customer.getCpf(), orders.get(0).getCustomer().getCpf());
        verify(jpaOrdersRepository, times(1)).findAllByCustomer(any(CustomerEntity.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersFoundForCustomer() {
        Customer customer = customerMock();
        when(jpaOrdersRepository.findAllByCustomer(any(CustomerEntity.class))).thenReturn(Collections.emptyList());

        List<Order> orders = orderRepository.findOrdersByCustomer(customer);

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
        verify(jpaOrdersRepository, times(1)).findAllByCustomer(any(CustomerEntity.class));
    }

    @Test
    void shouldFindOrderById() {
        Long orderId = 1L;
        Order order = orderMock();
        order.setId(orderId);
        OrderEntity orderEntity = fromOrderToOrderEntity(order);
        when(jpaOrdersRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));

        Order foundOrder = orderRepository.findOrderById(orderId);

        assertNotNull(foundOrder);
        assertEquals(orderId, foundOrder.getId());
        verify(jpaOrdersRepository, times(1)).findById(orderId);
    }

    @Test
    void shouldReturnNullWhenOrderNotFoundById() {
        Long orderId = 99L;
        when(jpaOrdersRepository.findById(orderId)).thenReturn(Optional.empty());

        Order foundOrder = orderRepository.findOrderById(orderId);

        assertNull(foundOrder);
        verify(jpaOrdersRepository, times(1)).findById(orderId);
    }

    @Test
    void shouldUpdateOrderSuccessfully() {
        Order order = orderMock();
        order.setStatus(OrderStatusEnum.FINISHED);
        OrderEntity orderEntity = fromOrderToOrderEntity(order);
        when(jpaOrdersRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        Order updatedOrder = orderRepository.updateOrder(order);

        assertNotNull(updatedOrder);
        assertEquals(order.getId(), updatedOrder.getId());
        assertEquals(OrderStatusEnum.FINISHED, updatedOrder.getStatus());
        verify(jpaOrdersRepository, times(1)).save(any(OrderEntity.class));
    }
}
