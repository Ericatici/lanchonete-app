package com.lanchonete.na.comanda.core.application.dto;

import java.math.BigDecimal;

import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.product.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
    private String itemId;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductCategoryEnum category;

    public Product toProduct() { 
        return Product.builder()
                .itemId(this.itemId)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .category(this.category)
                .build();
    }
}
