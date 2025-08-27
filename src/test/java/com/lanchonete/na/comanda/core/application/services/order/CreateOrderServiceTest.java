package com.lanchonete.na.comanda.core.application.services.order;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.CPF;
import static com.lanchonete.na.comanda.mocks.dto.OrderDTOMock.orderDTOMock;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static com.lanchonete.na.comanda.mocks.order.OrderItemMock.orderItemMock;
import static com.lanchonete.na.comanda.mocks.customer.CustomerMock.customerMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.application.services.customer.FindCustomerService;
import com.lanchonete.na.comanda.core.application.usecases.payment.GeneratePaymentQrCodeUseCase;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.InvalidOrderException;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;

public class CreateOrderServiceTest {

    @InjectMocks
    private CreateOrderService createOrderService;

    @Mock
    private OrderRepository ordersRepository;

    @Mock
    private FindCustomerService findCustomerService;

    @Mock
    private CreateOrderItemService createOrderItemService;

    @Mock
    private GeneratePaymentQrCodeUseCase generatePaymentQrCodeUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrderWhenCustomerExistsAndItemsAreValid() {
        final OrderDTO orderDTO = orderDTOMock();
        final Customer customer = customerMock();
        final OrderItem orderItem = orderItemMock();
        final Order savedOrder = orderMock();
        savedOrder.setPaymentStatus(PaymentStatusEnum.PENDING);
        savedOrder.setOrderDate(LocalDateTime.now());

        when(findCustomerService.getCustomerByCpf(CPF)).thenReturn(customer);
        when(createOrderItemService.createOrderItem(any(Order.class), any(OrderDTO.OrderItemDTO.class)))
                .thenReturn(orderItem);
        when(ordersRepository.saveOrder(any(Order.class))).thenReturn(savedOrder);
        when(generatePaymentQrCodeUseCase.generatePaymentQrCode(any(Order.class))).thenReturn(savedOrder);
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(savedOrder);

        final Order result = createOrderService.createOrder(orderDTO);

        assertEquals(1L, result.getId());
        assertEquals(CPF, result.getCustomer().getCpf());
        assertEquals(BigDecimal.valueOf(10), result.getTotalPrice()); 
        assertEquals(PaymentStatusEnum.PENDING, result.getPaymentStatus()); 
        assertNotNull(result.getOrderDate()); 
        verify(ordersRepository, times(1)).saveOrder(any(Order.class));
        verify(generatePaymentQrCodeUseCase, times(1)).generatePaymentQrCode(any(Order.class));
        verify(ordersRepository, times(1)).updateOrder(any(Order.class));
    }

    @Test
    void shouldCreateOrderWhenCustomerDoesNotExist() {
        final OrderDTO orderDTO = orderDTOMock();
        orderDTO.setCustomerCpf(null);
        final OrderItem orderItem = orderItemMock();
        final Order savedOrder = orderMock();
        savedOrder.setCustomer(null);
        savedOrder.setPaymentStatus(PaymentStatusEnum.PENDING);
        savedOrder.setOrderDate(LocalDateTime.now());

        when(findCustomerService.getCustomerByCpf(null)).thenReturn(null);
        when(createOrderItemService.createOrderItem(any(Order.class), any(OrderDTO.OrderItemDTO.class)))
                .thenReturn(orderItem);
        when(ordersRepository.saveOrder(any(Order.class))).thenReturn(savedOrder);
        when(generatePaymentQrCodeUseCase.generatePaymentQrCode(any(Order.class))).thenReturn(savedOrder);
        when(ordersRepository.updateOrder(any(Order.class))).thenReturn(savedOrder);

        final Order result = createOrderService.createOrder(orderDTO);

        assertEquals(1L, result.getId());
        assertEquals(null, result.getCustomer());
        assertEquals(BigDecimal.valueOf(10), result.getTotalPrice()); 
        assertEquals(PaymentStatusEnum.PENDING, result.getPaymentStatus()); 
        assertNotNull(result.getOrderDate()); 
        verify(ordersRepository, times(1)).saveOrder(any(Order.class));
        verify(generatePaymentQrCodeUseCase, times(1)).generatePaymentQrCode(any(Order.class)); 
        verify(ordersRepository, times(1)).updateOrder(any(Order.class));
    }

    @Test
    void shouldNotCreateOrderWhenOrderItemsIsEmpty() {
        final OrderDTO orderDTO = orderDTOMock();
        orderDTO.setItems(new ArrayList<>());

        assertThrows(
                InvalidOrderException.class, 
                () -> createOrderService.createOrder(orderDTO)
        );

        verify(ordersRepository, times(0)).saveOrder(any(Order.class)); 
        verify(generatePaymentQrCodeUseCase, times(0)).generatePaymentQrCode(any(Order.class)); 
        verify(ordersRepository, times(0)).updateOrder(any(Order.class));
    }

    @Test
    void shouldHandleExceptionDuringPaymentQrCodeGeneration() {
        final OrderDTO orderDTO = orderDTOMock();
        final OrderItem orderItem = orderItemMock();
        final Order savedOrder = orderMock();

        when(findCustomerService.getCustomerByCpf(CPF)).thenReturn(customerMock());
        when(createOrderItemService.createOrderItem(any(Order.class), any(OrderDTO.OrderItemDTO.class)))
                .thenReturn(orderItem);
        when(ordersRepository.saveOrder(any(Order.class))).thenReturn(savedOrder);
        when(generatePaymentQrCodeUseCase.generatePaymentQrCode(any(Order.class)))
                .thenThrow(new RuntimeException("Payment QR code generation failed"));

        assertThrows(RuntimeException.class, () -> createOrderService.createOrder(orderDTO));

        verify(ordersRepository, times(1)).saveOrder(any(Order.class));
        verify(generatePaymentQrCodeUseCase, times(1)).generatePaymentQrCode(any(Order.class));
        verify(ordersRepository, times(0)).updateOrder(any(Order.class)); 
    }

    @Test
    void shouldHandleExceptionDuringOrderUpdateAfterQrCodeGeneration() {
        final OrderDTO orderDTO = orderDTOMock();
        final OrderItem orderItem = orderItemMock();
        final Order savedOrder = orderMock();

        when(findCustomerService.getCustomerByCpf(CPF)).thenReturn(customerMock());
        when(createOrderItemService.createOrderItem(any(Order.class), any(OrderDTO.OrderItemDTO.class)))
                .thenReturn(orderItem);
        when(ordersRepository.saveOrder(any(Order.class))).thenReturn(savedOrder);
        when(generatePaymentQrCodeUseCase.generatePaymentQrCode(any(Order.class))).thenReturn(savedOrder);
        when(ordersRepository.updateOrder(any(Order.class)))
                .thenThrow(new RuntimeException("Order update failed after QR code generation"));

        assertThrows(RuntimeException.class, () -> createOrderService.createOrder(orderDTO));

        verify(ordersRepository, times(1)).saveOrder(any(Order.class));
        verify(generatePaymentQrCodeUseCase, times(1)).generatePaymentQrCode(any(Order.class));
        verify(ordersRepository, times(1)).updateOrder(any(Order.class));
    }

}
