package com.lanchonete.na.comanda.core.domain.product;

import java.math.BigDecimal;
import java.time.Instant;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.ProductResponse;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Product {

    private String itemId;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductCategoryEnum category;
    private Instant createdDate;
    private Instant updatedDate;

    public ProductResponse toProductResponse() { 
        return ProductResponse.builder()
                .itemId(this.itemId)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .category(this.category)
                .build();
    }
    
}
