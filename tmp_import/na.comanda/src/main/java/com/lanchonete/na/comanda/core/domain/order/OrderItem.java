package com.lanchonete.na.comanda.core.domain.order;

import java.math.BigDecimal;
import java.time.Instant;

import com.lanchonete.na.comanda.core.domain.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderItem {

    private Long id;
    private Product product;
    private Integer quantity;
    private BigDecimal itemPrice;
    private Instant createdDate;
    private Instant updatedDate;

    public String getItemId() {
        return product.getItemId();
    }
    
}
