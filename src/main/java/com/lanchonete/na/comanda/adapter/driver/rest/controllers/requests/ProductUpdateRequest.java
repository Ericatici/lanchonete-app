package com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests;

import java.math.BigDecimal;

import com.lanchonete.na.comanda.core.application.dto.ProductDTO;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @Positive(message = "Price must be positive")
    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Category is required")
    private ProductCategoryEnum category;

    public ProductDTO toDto(String id) { 
        return ProductDTO.builder()
                .itemId(id)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .category(this.category)
                .build();
    }
}
