package com.lanchonete.na.comanda.adapter.driver.rest.controllers.mvc;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.mocks.request.OrderRequestMock.orderRequestMock;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.OrderController;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.OrderRequest;
import com.lanchonete.na.comanda.core.application.services.order.CreateOrderService;
import com.lanchonete.na.comanda.core.application.services.order.FindOrderService;
import com.lanchonete.na.comanda.core.application.services.order.UpdateOrderService;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;


@WebMvcTest(OrderController.class)
public class OrderControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateOrderService createOrderService;

    @MockitoBean
    private FindOrderService findOrderService;

    @MockitoBean
    private UpdateOrderService updateOrderService;


    @Test
    void shouldCreateOrderSuccessfullyWhenInputIsValid() throws Exception {
        when(createOrderService.createOrder(any())).thenReturn(orderMock());

        mockMvc.perform(MockMvcRequestBuilders.post("/order/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestMock())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ORDER_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerCpf").value(CPF));
    }

    @Test
    void shouldReturnBadRequestWhenItemsListIsEmpty() throws Exception {
        OrderRequest invalidRequest = OrderRequest.builder()
                .customerCpf(CPF)
                .items(Collections.emptyList()) 
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/order/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenProductIdIsMissing() throws Exception {
        OrderRequest invalidRequest = OrderRequest.builder()
                .customerCpf(CPF)
                .items(Collections.singletonList(OrderRequest.OrderItemRequest.builder()
                        .quantity(2)
                        .build())) 
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/order/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenQuantityIsMissing() throws Exception {
        OrderRequest invalidRequest = OrderRequest.builder()
                .customerCpf(CPF)
                .items(Collections.singletonList(OrderRequest.OrderItemRequest.builder()
                        .productId(ITEM_ID)
                        .build())) 
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/order/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    
    @Test
    void shouldGetAllOrdersSuccessfullyWhenOrdersExist() throws Exception {
        when(findOrderService.getAllOrders()).thenReturn(Collections.singletonList(orderMock()));

        mockMvc.perform(MockMvcRequestBuilders.get("/order"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(ORDER_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerCpf").value(CPF));
    }

    @Test
    void shouldReturnOkWithEmptyListWhenNoOrdersExist() throws Exception {
        when(findOrderService.getAllOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/order"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void shouldGetOrdersByStatusSuccessfullyWhenStatusIsValid() throws Exception {
        when(findOrderService.getAllOrdersByStatus(OrderStatusEnum.RECEIVED))
                .thenReturn(Collections.singletonList(orderMock(OrderStatusEnum.RECEIVED)));

        mockMvc.perform(MockMvcRequestBuilders.get("/order/status/RECEIVED"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(ORDER_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("RECEIVED"));
    }

    @Test
    void shouldGetOrderByIdSuccessfullyWhenOrderExists() throws Exception {
        when(findOrderService.getOrderById(1L)).thenReturn(orderMock());

        mockMvc.perform(MockMvcRequestBuilders.get("/order/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ORDER_ID));
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        when(findOrderService.getOrderById(999L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/order/999"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void shouldUpdateOrderStatusSuccessfullyWhenInputIsValid() throws Exception {
        when(updateOrderService.updateOrderStatusById(1L, OrderStatusEnum.IN_PREPARATION))
                .thenReturn(orderMock(OrderStatusEnum.IN_PREPARATION));

        mockMvc.perform(MockMvcRequestBuilders.patch("/order/1/status/IN_PREPARATION"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PREPARATION")); 
    }

    @Test
    void shouldUpdateOrderSuccessfullyWhenInputIsValid() throws Exception {
        when(updateOrderService.updateOrder(anyLong(), any())).thenReturn(orderMock());

        mockMvc.perform(MockMvcRequestBuilders.patch("/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestMock())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ORDER_ID));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingOrderWithEmptyItems() throws Exception {
        OrderRequest invalidRequest = OrderRequest.builder()
                .customerCpf(CPF)
                .items(Collections.emptyList())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnNoContentWhenNoActiveOrdersExist() throws Exception {
        when(findOrderService.getAllActiveOrdersSorted()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/order/activeOrders"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldReturnOkWithActiveOrdersWhenActiveOrdersExist() throws Exception {
        List<Order> activeOrders = Collections.singletonList(orderMock(OrderStatusEnum.IN_PREPARATION));
        when(findOrderService.getAllActiveOrdersSorted()).thenReturn(activeOrders);

        mockMvc.perform(MockMvcRequestBuilders.get("/order/activeOrders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(ORDER_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(OrderStatusEnum.IN_PREPARATION.name()));
    }
}
