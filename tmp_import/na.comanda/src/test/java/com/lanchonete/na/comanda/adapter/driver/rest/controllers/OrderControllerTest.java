package com.lanchonete.na.comanda.adapter.driver.rest.controllers;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.REQUEST_TRACE_ID;
import static com.lanchonete.na.comanda.mocks.dto.OrderDTOMock.orderDTOMock;
import static com.lanchonete.na.comanda.mocks.request.OrderRequestMock.orderRequestMock;
import static com.lanchonete.na.comanda.mocks.response.OrderResponseMock.orderResponseMock;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.OrderRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.OrderResponse;
import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.application.services.order.CreateOrderService;
import com.lanchonete.na.comanda.core.application.services.order.FindOrderService;
import com.lanchonete.na.comanda.core.application.services.order.UpdateOrderService;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;


public class OrderControllerTest {

    @Mock
    private CreateOrderService createOrderService;

    @Mock
    private FindOrderService findOrderService;

    @Mock
    private UpdateOrderService updateOrderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void shouldReturnsCreatedWhenCreateOrderAndValidRequest() {
        final OrderRequest orderRequest = orderRequestMock();
        final OrderResponse orderResponse = orderResponseMock();
        final Order order = orderMock();
        final OrderDTO orderDTO = orderDTOMock();

        when(createOrderService.createOrder(orderDTO)).thenReturn(order);

        ResponseEntity<OrderResponse> response = orderController.checkout(REQUEST_TRACE_ID, orderRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderResponse, response.getBody());
    }

    @Test
    void shouldReturnsOkWithOrderWhenGetAllOrdersAndOrdersExists() {
        final List<Order> orders = Collections.singletonList(orderMock());
        final List<OrderResponse> orderResponses = Collections.singletonList(orderResponseMock());

        when(findOrderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<List<OrderResponse>> response = orderController.getAllOrders(REQUEST_TRACE_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderResponses, response.getBody());
    }

    @Test
    void shouldReturnsOkWhenGetAllOrdersAndNoOrdersExist() {
        when(findOrderService.getAllOrders()).thenReturn(Collections.emptyList());

        ResponseEntity<List<OrderResponse>> response = orderController.getAllOrders(REQUEST_TRACE_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldGetOrderByIdWhenOrderExistsReturnsOkWithOrder() {
        final OrderResponse orderResponse = orderResponseMock();
        final Order order = orderMock();

        when(findOrderService.getOrderById(ORDER_ID)).thenReturn(order);

        ResponseEntity<OrderResponse> response = orderController.getOrderById(REQUEST_TRACE_ID, ORDER_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderResponse, response.getBody());
    }


    @Test
    void shouldReturnsOkWithOrdersWhenGetOrdersByStatusAndOrdersExistWithStatus() {
        final OrderStatusEnum status = OrderStatusEnum.RECEIVED;
        final List<OrderResponse> orderResponses = Collections.singletonList(orderResponseMock());
        final List<Order> orders = Collections.singletonList(orderMock());

        when(findOrderService.getAllOrdersByStatus(status)).thenReturn(orders);

        ResponseEntity<List<OrderResponse>> response = orderController.getOrdersByStatus(REQUEST_TRACE_ID, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderResponses, response.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenGetOrdersByStatusAndNoOrdersExistWithStatus() {
        final OrderStatusEnum status = OrderStatusEnum.RECEIVED;

        when(findOrderService.getAllOrdersByStatus(status)).thenReturn(Collections.emptyList());

        ResponseEntity<List<OrderResponse>> response = orderController.getOrdersByStatus(REQUEST_TRACE_ID, status);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturnsOkWithUpdatedOrderWhenUpdateOrderStatusAndOrderExistsAndStatusIsValid() {
        final OrderStatusEnum newStatus = OrderStatusEnum.IN_PREPARATION;
        final OrderResponse updatedOrderResponse = orderResponseMock();
        final Order order = orderMock();

        when(updateOrderService.updateOrderStatusById(ORDER_ID, newStatus)).thenReturn(order);

        ResponseEntity<OrderResponse> response = orderController.updateOrderStatus(REQUEST_TRACE_ID, ORDER_ID, newStatus);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOrderResponse, response.getBody());
    }

    @Test
    void shouldUpdateOrderWhenRequestIsValid() {
        final OrderRequest orderUpdateRequest = orderRequestMock();
        final OrderResponse expectedResponse = orderResponseMock();
        final Order order = orderMock();

        when(updateOrderService.updateOrder(eq(ORDER_ID), any())).thenReturn(order);

        ResponseEntity<OrderResponse> responseEntity = orderController.updateOrder(REQUEST_TRACE_ID, ORDER_ID, orderUpdateRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(updateOrderService, times(1)).updateOrder(eq(ORDER_ID), any());
    }

    @Test
    void shouldHandleExceptionWhenUpdateOrderServiceThrowsAnException() {
        final OrderRequest orderUpdateRequest = orderRequestMock();
        orderUpdateRequest.setItems(Collections.emptyList());

        when(updateOrderService.updateOrder(eq(ORDER_ID), any())).thenThrow(new RuntimeException("Simulated error"));

        assertThrows(RuntimeException.class, () -> orderController.updateOrder(null, ORDER_ID, orderUpdateRequest));
    }

    @Test
    void shouldGetAllActiveOrdersReturnsNoContentWhenNoActiveOrders() {
        when(findOrderService.getAllActiveOrdersSorted()).thenReturn(Collections.emptyList());

        ResponseEntity<List<OrderResponse>> response = orderController.getAllActiveOrders(REQUEST_TRACE_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldGetAllActiveOrdersReturnsOkWithOrdersWhenActiveOrdersExist() {
        final List<Order> orders = Arrays.asList(orderMock(OrderStatusEnum.IN_PREPARATION), orderMock(OrderStatusEnum.READY));
        final List<OrderResponse> orderResponses = Arrays.asList(orderResponseMock(OrderStatusEnum.IN_PREPARATION), orderResponseMock(OrderStatusEnum.READY));

        when(findOrderService.getAllActiveOrdersSorted()).thenReturn(orders);

        ResponseEntity<List<OrderResponse>> response = orderController.getAllActiveOrders(REQUEST_TRACE_ID);
        List<OrderResponse> responses = response.getBody();

        assertNotNull(responses);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderResponses.size(), responses.size());
        assertEquals(orderResponses.get(0).getStatus(), responses.get(0).getStatus());
    }
}
