package com.lanchonete.na.comanda.core.application.services.order;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF_2;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.mocks.dto.OrderDTOMock.orderDTOMock;
import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static com.lanchonete.na.comanda.mocks.order.OrderItemMock.orderItemMock;
import static com.lanchonete.na.comanda.mocks.dto.OrderItemDTOMock.orderItemDTOMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.application.dto.OrderDTO.OrderItemDTO;
import com.lanchonete.na.comanda.core.application.services.customer.FindCustomerService;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.InvalidOrderException;
import com.lanchonete.na.comanda.core.domain.exeptions.OrderNotFoundException;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;

public class UpdateOrderStatusServiceTest {
    
    @InjectMocks
    private UpdateOrderService updateOrderService;

    @Mock
    private OrderRepository ordersRepository;

    @Mock
    private FindCustomerService findCustomerService;

    @Mock
    private CreateOrderItemService createOrderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUpdateOrderStatusWhenOrderExistsAndStatusIsDifferent() {
        final Order foundOrder = orderMock();
        foundOrder.setStatus(OrderStatusEnum.IN_PREPARATION);

        final Order newOrder = orderMock();
        newOrder.setStatus(OrderStatusEnum.READY);
        
        when(ordersRepository.findOrderById(1L)).thenReturn(foundOrder);
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(newOrder);

        Order result = updateOrderService.updateOrderStatusById(1L, OrderStatusEnum.READY);

        assertEquals(OrderStatusEnum.READY, result.getStatus());
        verify(ordersRepository, times(1)).findOrderById(1L);
        verify(ordersRepository, times(1)).updateOrder(any(Order.class));
    }

    @Test
    void shouldReturnOrderWhenOrderExistsAndStatusIsSame() {
        final OrderStatusEnum status = OrderStatusEnum.IN_PREPARATION;
        final Order order = orderMock();
        order.setStatus(status);
        
        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);

        Order result = updateOrderService.updateOrderStatusById(ORDER_ID, status);

        assertEquals(status, result.getStatus());
        verify(ordersRepository, times(1)).findOrderById(ORDER_ID);
        verify(ordersRepository, never()).updateOrder(any(Order.class));
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderDoesNotExistForStatusUpdate() {
        final OrderStatusEnum status = OrderStatusEnum.IN_PREPARATION;
        
        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> updateOrderService.updateOrderStatusById(ORDER_ID, status));
        verify(ordersRepository, times(1)).findOrderById(ORDER_ID);
        verify(ordersRepository, never()).updateOrder(any(Order.class));
    }

    @Test
    void shouldAllowValidStatusTransitionFromWaitingPaymentToReceived() {
        Order order = orderMock();
        order.setStatus(OrderStatusEnum.WAITING_PAYMENT);

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(order);

        Order updatedOrder = updateOrderService.updateOrderStatusById(ORDER_ID, OrderStatusEnum.RECEIVED);
        assertNotNull(updatedOrder);
        assertEquals(OrderStatusEnum.RECEIVED, updatedOrder.getStatus());
        verify(ordersRepository, times(1)).updateOrder(any(Order.class));
    }

     @Test
    void shouldAllowValidStatusTransitionFromReceivedToInPreparation() {
        Order order = orderMock();
        order.setStatus(OrderStatusEnum.RECEIVED);

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(order);

        Order updatedOrder = updateOrderService.updateOrderStatusById(ORDER_ID, OrderStatusEnum.IN_PREPARATION);
        assertNotNull(updatedOrder);
        assertEquals(OrderStatusEnum.IN_PREPARATION, updatedOrder.getStatus());
        verify(ordersRepository, times(1)).updateOrder(any(Order.class));
    }

    @Test
    void shouldAllowValidStatusTransitionFromInPreparationToReady() {
        Order order = orderMock();
        order.setStatus(OrderStatusEnum.IN_PREPARATION);

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(order);

        Order updatedOrder = updateOrderService.updateOrderStatusById(ORDER_ID, OrderStatusEnum.READY);
        assertNotNull(updatedOrder);
        assertEquals(OrderStatusEnum.READY, updatedOrder.getStatus());
        verify(ordersRepository, times(1)).updateOrder(any(Order.class));
    }

    @Test
    void shouldAllowValidStatusTransitionFromReadyToFinished() {
        Order order = orderMock();
        order.setStatus(OrderStatusEnum.READY);

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(order);

        Order updatedOrder = updateOrderService.updateOrderStatusById(ORDER_ID, OrderStatusEnum.FINISHED);
        assertNotNull(updatedOrder);
        assertEquals(OrderStatusEnum.FINISHED, updatedOrder.getStatus());
        verify(ordersRepository, times(1)).updateOrder(any(Order.class));
    }

    @Test
    void shouldAllowTransitionToCancelledFromAnyActiveStatus() {
        Order order1 = orderMock();
        order1.setStatus(OrderStatusEnum.WAITING_PAYMENT);
        when(ordersRepository.findOrderById(1L)).thenReturn(order1);
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(order1);
        updateOrderService.updateOrderStatusById(1L, OrderStatusEnum.CANCELLED);
        assertEquals(OrderStatusEnum.CANCELLED, order1.getStatus());

        Order order2 = orderMock();
        order2.setId(2L);
        order2.setStatus(OrderStatusEnum.RECEIVED);
        when(ordersRepository.findOrderById(2L)).thenReturn(order2);
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(order2);
        updateOrderService.updateOrderStatusById(2L, OrderStatusEnum.CANCELLED);
        assertEquals(OrderStatusEnum.CANCELLED, order2.getStatus());

        Order order3 = orderMock();
        order3.setId(3L);
        order3.setStatus(OrderStatusEnum.IN_PREPARATION);
        when(ordersRepository.findOrderById(3L)).thenReturn(order3);
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(order3);
        updateOrderService.updateOrderStatusById(3L, OrderStatusEnum.CANCELLED);
        assertEquals(OrderStatusEnum.CANCELLED, order3.getStatus());
        
        verify(ordersRepository, times(3)).updateOrder(any(Order.class));
    }

    @Test
    void shouldThrowInvalidOrderExceptionForInvalidStatusTransition() {
        Order order = orderMock();
        order.setStatus(OrderStatusEnum.WAITING_PAYMENT);

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);

        assertThrows(InvalidOrderException.class, 
                     () -> updateOrderService.updateOrderStatusById(ORDER_ID, OrderStatusEnum.READY),
                     "Invalid order status transition from WAITING_PAYMENT to READY");
        verify(ordersRepository, never()).updateOrder(any(Order.class));
    }

    @Test
    void shouldThrowInvalidOrderExceptionWhenTransitioningToPreviousStatus() {
        Order order = orderMock();
        order.setStatus(OrderStatusEnum.READY); 

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);

        assertThrows(InvalidOrderException.class, 
                     () -> updateOrderService.updateOrderStatusById(ORDER_ID, OrderStatusEnum.RECEIVED),
                     "Cannot transition order status from READY to a previous status RECEIVED");
        verify(ordersRepository, never()).updateOrder(any(Order.class));
    }

    @Test
    void shouldNotAllowTransitionFromFinished() {
        Order order = orderMock();
        order.setStatus(OrderStatusEnum.FINISHED);

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);

        assertThrows(InvalidOrderException.class, 
                     () -> updateOrderService.updateOrderStatusById(ORDER_ID, OrderStatusEnum.READY));
        verify(ordersRepository, never()).updateOrder(any(Order.class));
    }

    @Test
    void shouldNotAllowTransitionFromCancelled() {
        Order order = orderMock();
        order.setStatus(OrderStatusEnum.CANCELLED);

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(order);

        assertThrows(InvalidOrderException.class, 
                     () -> updateOrderService.updateOrderStatusById(ORDER_ID, OrderStatusEnum.READY));
        verify(ordersRepository, never()).updateOrder(any(Order.class));
    }

    @Test
    void shouldUpdateOrderWhenOrderExistsAndHasChanges() {
        final OrderDTO orderDTO = orderDTOMock();
        orderDTO.setCustomerCpf(CPF_2);

        final Customer existingCustomer = customerMock();
        final Customer newCustomer = customerMock(CPF_2);

        final Order existingOrder = orderMock();
        existingOrder.setCustomer(existingCustomer);
    
        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(existingOrder);
        when(findCustomerService.getCustomerByCpf(CPF_2)).thenReturn(newCustomer);
        when(createOrderItemService.createOrderItem(any(Order.class), any(OrderItemDTO.class)))
                .thenReturn(orderItemMock());
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(existingOrder);

        Order result = updateOrderService.updateOrder(ORDER_ID, orderDTO);

        assertEquals(CPF_2, result.getCustomer().getCpf());
        verify(ordersRepository, times(1)).findOrderById(ORDER_ID);
        verify(findCustomerService, times(1)).getCustomerByCpf(CPF_2);
        verify(createOrderItemService, times(1)).createOrderItem(any(Order.class), any(OrderItemDTO.class));
        verify(ordersRepository, times(1)).updateOrder(any(Order.class));
    }

    @Test
    void shouldReturnExistingOrderWhenOrderExistsAndHasNoChanges() {
        final OrderDTO orderDTO = orderDTOMock();
        final Order existingOrder = orderMock();

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(existingOrder);

        Order result = updateOrderService.updateOrder(ORDER_ID, orderDTO);

        assertEquals(CPF, result.getCustomer().getCpf());
        verify(ordersRepository, times(1)).findOrderById(ORDER_ID);
        verify(findCustomerService, never()).getCustomerByCpf(anyString());
        verify(createOrderItemService, never()).createOrderItem(any(Order.class), any(OrderItemDTO.class));
        verify(ordersRepository, never()).updateOrder(any(Order.class));
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderDoesNotExistForUpdate() {
        final OrderDTO orderDTO = orderDTOMock();

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> updateOrderService.updateOrder(ORDER_ID, orderDTO));
        verify(ordersRepository, times(1)).findOrderById(ORDER_ID);
        verify(findCustomerService, never()).getCustomerByCpf(anyString());
        verify(createOrderItemService, never()).createOrderItem(any(Order.class), any(OrderItemDTO.class));
        verify(ordersRepository, never()).updateOrder(any(Order.class));
    }

    @Test
    void shouldReturnTrueWhenCustomerCpfChanges() {
        Order existingOrder = orderMock();
        existingOrder.setCustomer(customerMock("12345678900"));

        OrderDTO orderDTO = orderDTOMock();
        orderDTO.setCustomerCpf("00987654321"); 

        
        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(existingOrder);
        when(findCustomerService.getCustomerByCpf(anyString())).thenReturn(customerMock("00987654321"));
        when(createOrderItemService.createOrderItem(any(), any())).thenReturn(orderItemMock());
        when(ordersRepository.updateOrder(any())).thenReturn(existingOrder);

        Order result = updateOrderService.updateOrder(ORDER_ID, orderDTO);
        assertNotNull(result);
        assertEquals("00987654321", result.getCustomer().getCpf());
        verify(ordersRepository, times(1)).updateOrder(any(Order.class)); 
    }

    @Test
    void shouldReturnTrueWhenNumberOfItemsChanges() {
        Order existingOrder = orderMock();
        existingOrder.setCustomer(customerMock(CPF));
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItemMock());
        existingOrder.setItems(orderItemList);

        OrderDTO orderDTO = orderDTOMock();
        orderDTO.setCustomerCpf(CPF); 
        orderDTO.setItems(Arrays.asList(orderItemDTOMock(), orderItemDTOMock())); 

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(existingOrder);
        when(findCustomerService.getCustomerByCpf(anyString())).thenReturn(customerMock(CPF));
        when(createOrderItemService.createOrderItem(any(), any())).thenReturn(orderItemMock());
        when(ordersRepository.updateOrder(any())).thenReturn(existingOrder);

        Order result = updateOrderService.updateOrder(ORDER_ID, orderDTO);
        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        verify(ordersRepository, times(1)).updateOrder(any(Order.class)); 
    }

    @Test
    void shouldReturnTrueWhenItemQuantitiesChange() {
        Order existingOrder = orderMock();
        existingOrder.setCustomer(customerMock(CPF));
        OrderItem existingItem = orderItemMock();
        existingItem.setQuantity(1);
        existingItem.setItemPrice(BigDecimal.valueOf(10)); 
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(existingItem);
        existingOrder.setItems(orderItemList);

        OrderDTO orderDTO = orderDTOMock();
        orderDTO.setCustomerCpf(CPF); 
        OrderItemDTO changedItemDTO = orderItemDTOMock();
        changedItemDTO.setQuantity(3); 
        orderDTO.setItems(Arrays.asList(changedItemDTO));

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(existingOrder);
        when(findCustomerService.getCustomerByCpf(anyString())).thenReturn(customerMock(CPF));
        when(createOrderItemService.createOrderItem(any(), any())).thenReturn(orderItemMock()); 
        when(ordersRepository.updateOrder(any())).thenReturn(existingOrder);

        Order result = updateOrderService.updateOrder(ORDER_ID, orderDTO);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(2, result.getItems().get(0).getQuantity()); 
        verify(ordersRepository, times(1)).updateOrder(any(Order.class)); 
    }

    @Test
    void shouldReturnTrueWhenProductIdsChange() {
        Order existingOrder = orderMock();
        existingOrder.setCustomer(customerMock(CPF));
        OrderItem existingItem = orderItemMock();
        existingItem.getProduct().setItemId("productA"); 
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(existingItem);
        existingOrder.setItems(orderItemList);

        OrderDTO orderDTO = orderDTOMock();
        orderDTO.setCustomerCpf(CPF); 
        OrderItemDTO changedItemDTO = orderItemDTOMock();
        changedItemDTO.setProductId("productB"); 
        orderDTO.setItems(Arrays.asList(changedItemDTO));

        when(ordersRepository.findOrderById(ORDER_ID)).thenReturn(existingOrder);
        when(findCustomerService.getCustomerByCpf(anyString())).thenReturn(customerMock(CPF));
        OrderItem newItem = orderItemMock();
        newItem.getProduct().setItemId("productB");
        when(createOrderItemService.createOrderItem(any(), any())).thenReturn(newItem); 
        when(ordersRepository.updateOrder(any())).thenReturn(existingOrder);

        Order result = updateOrderService.updateOrder(ORDER_ID, orderDTO);
        
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("productB", result.getItems().get(0).getItemId()); 
        verify(ordersRepository, times(1)).updateOrder(any(Order.class)); 
    }

  
    @Test
    void shouldUpdateOrderDirectly() {
        Order orderToUpdate = orderMock();
        orderToUpdate.setStatus(OrderStatusEnum.IN_PREPARATION); 

        Order updatedOrder = orderMock();
        updatedOrder.setStatus(OrderStatusEnum.IN_PREPARATION);

        when(ordersRepository.updateOrder(orderToUpdate)).thenReturn(updatedOrder);

        Order result = updateOrderService.updateOrder(orderToUpdate);

        assertNotNull(result);
        assertEquals(OrderStatusEnum.IN_PREPARATION, result.getStatus());
        verify(ordersRepository, times(1)).updateOrder(orderToUpdate);
    }
}
