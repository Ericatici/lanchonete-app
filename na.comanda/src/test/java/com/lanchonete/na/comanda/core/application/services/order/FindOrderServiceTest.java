package com.lanchonete.na.comanda.core.application.services.order;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.OrderNotFoundException;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;

public class FindOrderServiceTest {
    
    @InjectMocks
    private FindOrderService findOrderService;

    @Mock
    private OrderRepository ordersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllOrdersByStatusWhenOrdersExist() {
        final OrderStatusEnum status = OrderStatusEnum.RECEIVED;
        final Order order = orderMock();
       
        when(ordersRepository.findOrdersByStatus(status)).thenReturn(Collections.singletonList(order));

        List<Order> result = findOrderService.getAllOrdersByStatus(status);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(ordersRepository, times(1)).findOrdersByStatus(status);
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenNoOrdersFoundByStatus() {
        final OrderStatusEnum status = OrderStatusEnum.RECEIVED;
       
        when(ordersRepository.findOrdersByStatus(status)).thenReturn(Collections.emptyList());

        assertThrows(OrderNotFoundException.class, () -> findOrderService.getAllOrdersByStatus(status));
        verify(ordersRepository, times(1)).findOrdersByStatus(status);
    }

    @Test
    void shouldGetAllOrdersWhenOrdersExist() {
        final Order order = orderMock();
        
        when(ordersRepository.findAllOrders()).thenReturn(Collections.singletonList(order));

        List<Order> result = findOrderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(ordersRepository, times(1)).findAllOrders();
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenNoOrdersFound() {
        when(ordersRepository.findAllOrders()).thenReturn(Collections.emptyList());

        assertThrows(OrderNotFoundException.class, () -> findOrderService.getAllOrders());
        verify(ordersRepository, times(1)).findAllOrders();
    }

    @Test
    void shouldGetAllOrdersByCustomerWhenOrdersExistForCustomer() {
        final Customer customer = new Customer();
        final Order order = orderMock();
        
        when(ordersRepository.findOrdersByCustomer(customer)).thenReturn(Collections.singletonList(order));

        List<Order> result = findOrderService.getAllOrdersByCustomer(customer);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(ordersRepository, times(1)).findOrdersByCustomer(customer);
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenNoOrdersFoundForCustomer() {
        final Customer customer = new Customer();
        
        when(ordersRepository.findOrdersByCustomer(customer)).thenReturn(Collections.emptyList());

        assertThrows(OrderNotFoundException.class, () -> findOrderService.getAllOrdersByCustomer(customer));
        verify(ordersRepository, times(1)).findOrdersByCustomer(customer);
    }

    @Test
    void shouldGetOrderByIdWhenOrderExists() {
        final Order order = orderMock();
        order.setId(ORDER_ID);
        
        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);

        Order result = findOrderService.getOrderById(ORDER_ID);

        assertNotNull(result);
        assertEquals(ORDER_ID, result.getId());
        verify(ordersRepository, times(1)).findOrderById(ORDER_ID);
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderDoesNotExist() {      
        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> findOrderService.getOrderById(ORDER_ID));
        verify(ordersRepository, times(1)).findOrderById(ORDER_ID);
    }

    @Test
    void shouldGetAllActiveOrdersSortedWhenOrdersExist() {
        Order orderReady1 = orderMock();
        orderReady1.setId(1L);
        orderReady1.setStatus(OrderStatusEnum.READY);
        orderReady1.setOrderDate(LocalDateTime.now().minusMinutes(10));

        Order orderReceived1 = orderMock();
        orderReceived1.setId(2L);
        orderReceived1.setStatus(OrderStatusEnum.RECEIVED);
        orderReceived1.setOrderDate(LocalDateTime.now().minusMinutes(5));

        Order orderInProgress1 = orderMock();
        orderInProgress1.setId(3L);
        orderInProgress1.setStatus(OrderStatusEnum.IN_PREPARATION);
        orderInProgress1.setOrderDate(LocalDateTime.now().minusMinutes(15));

        Order orderReady2 = orderMock();
        orderReady2.setId(4L);
        orderReady2.setStatus(OrderStatusEnum.READY);
        orderReady2.setOrderDate(LocalDateTime.now().minusMinutes(20));
        
        Order orderReceived2 = orderMock();
        orderReceived2.setId(5L);
        orderReceived2.setStatus(OrderStatusEnum.RECEIVED);
        orderReceived2.setOrderDate(LocalDateTime.now().minusMinutes(2));

        List<Order> unsortedOrders = Arrays.asList(orderInProgress1, orderReady1, orderReceived1, orderReady2, orderReceived2);
        
        when(ordersRepository.findOrdersByStatusList(any())).thenReturn(unsortedOrders);

        List<Order> result = findOrderService.getAllActiveOrdersSorted();

        assertNotNull(result);
        assertEquals(5, result.size());
        
        assertEquals(orderReady2.getId(), result.get(0).getId());
        assertEquals(orderReady1.getId(), result.get(1).getId());
        assertEquals(orderInProgress1.getId(), result.get(2).getId());
        assertEquals(orderReceived1.getId(), result.get(3).getId());
        assertEquals(orderReceived2.getId(), result.get(4).getId());


        verify(ordersRepository, times(1)).findOrdersByStatusList(any());
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveOrdersFound() {
        when(ordersRepository.findOrdersByStatusList(any())).thenReturn(Collections.emptyList());

        List<Order> result = findOrderService.getAllActiveOrdersSorted();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ordersRepository, times(1)).findOrdersByStatusList(any());
    }

    @Test
    void shouldSortOrdersCorrectlyByStatusAndDate() {
        Order orderReady = Order.builder()
            .id(1L)
            .status(OrderStatusEnum.READY)
            .orderDate(LocalDateTime.now().minusHours(1))
            .build();
   
        Order orderInProgress = Order.builder()
            .id(2L)
            .status(OrderStatusEnum.IN_PREPARATION)
            .orderDate(LocalDateTime.now().minusHours(2))
            .build();
  
        Order orderReceived = Order.builder()
            .id(3L)
            .status(OrderStatusEnum.RECEIVED)
            .orderDate(LocalDateTime.now().minusHours(3))
            .build();
  
        Order orderReadyNewer = Order.builder()
            .id(4L)
            .status(OrderStatusEnum.READY)
            .orderDate(LocalDateTime.now().minusMinutes(30))
            .build();
 
        List<Order> orders = Arrays.asList(orderInProgress, orderReceived, orderReady, orderReadyNewer);

        List<Order> sortedOrders = findOrderService.sortOrders(orders);

        assertEquals(4, sortedOrders.size());
        assertEquals(orderReady.getId(), sortedOrders.get(0).getId()); 
        assertEquals(orderReadyNewer.getId(), sortedOrders.get(1).getId()); 
        assertEquals(orderInProgress.getId(), sortedOrders.get(2).getId()); 
        assertEquals(orderReceived.getId(), sortedOrders.get(3).getId()); 
    }
}
