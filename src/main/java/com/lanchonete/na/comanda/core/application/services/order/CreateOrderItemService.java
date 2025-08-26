package com.lanchonete.na.comanda.core.application.services.order;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.application.usecases.order.CreateOrderItemUseCase;
import com.lanchonete.na.comanda.core.application.usecases.product.FindProductsUseCase;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;
import com.lanchonete.na.comanda.core.domain.product.Product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CreateOrderItemService implements CreateOrderItemUseCase {

    private final FindProductsUseCase findProductUseCase;

    @Override
    public OrderItem createOrderItem(final Order order, final OrderDTO.OrderItemDTO itemDTO) {
        log.info("Creating order item with product Id: {}", itemDTO.getProductId());

        Product product = findProductUseCase.findByItemId(itemDTO.getProductId());

        OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .itemPrice(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())))
                .build();

        return orderItem;
    }
    
}
