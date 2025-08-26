package com.lanchonete.na.comanda.core.application.services.order;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;
import static com.lanchonete.na.comanda.mocks.dto.OrderItemDTOMock.orderItemDTOMock;
import static com.lanchonete.na.comanda.mocks.order.OrderMock.orderMock;
import static com.lanchonete.na.comanda.mocks.product.ProductMock.productMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.application.services.product.FindProductService;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;
import com.lanchonete.na.comanda.core.domain.product.Product;

public class CreateOrderItemServiceTest {
    @InjectMocks
    private CreateOrderItemService createOrderItemService;

    @Mock
    private FindProductService findProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrderItemWhenProductIsFound() {
        final OrderDTO.OrderItemDTO itemDTO = orderItemDTOMock();
        final Order order = orderMock();        
        final Product product = productMock();

        when(findProductService.findByItemId(ITEM_ID)).thenReturn(product);

        OrderItem result = createOrderItemService.createOrderItem(order, itemDTO);

        assertNotNull(result); 
        assertEquals(product, result.getProduct());
        assertEquals(itemDTO.getQuantity(), result.getQuantity()); 
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())), result.getItemPrice()); 
        verify(findProductService, times(1)).findByItemId(ITEM_ID);
    }

    @Test
    void shouldThrowNullPointerExceptionWhenProductNotFound() { 
        final Order order = orderMock();
        final OrderDTO.OrderItemDTO itemDTO = orderItemDTOMock();

        when(findProductService.findByItemId(ITEM_ID)).thenReturn(null);

       assertThrows(
                NullPointerException.class, 
                () -> createOrderItemService.createOrderItem(order, itemDTO)
        );

        verify(findProductService, times(1)).findByItemId(ITEM_ID); 
    }

    @Test
    void shouldCreateOrderItemWithZeroQuantity() { 
        final OrderDTO.OrderItemDTO itemDTO = orderItemDTOMock();
        itemDTO.setQuantity(0);
        final Order order = orderMock();        
        final Product product = productMock();

        when(findProductService.findByItemId(ITEM_ID)).thenReturn(product);

        OrderItem result = createOrderItemService.createOrderItem(order, itemDTO);

        assertNotNull(result);
        assertEquals(product, result.getProduct());
        assertEquals(0, result.getQuantity());
        assertEquals(BigDecimal.ZERO, result.getItemPrice());
        verify(findProductService, times(1)).findByItemId(ITEM_ID);
    }
}