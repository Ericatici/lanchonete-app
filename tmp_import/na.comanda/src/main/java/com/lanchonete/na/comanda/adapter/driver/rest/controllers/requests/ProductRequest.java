package com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ProductRequest {

    @JsonProperty
    @NotBlank(message = "Item ID is required")
    private String itemId;

    @JsonProperty
    @NotBlank(message = "Name is required")
    private String name;

    @JsonProperty
    private String description;

    @JsonProperty
    @Positive(message = "Price must be positive")
    @NotNull(message = "Price is required")
    private BigDecimal price;

    @JsonProperty
    @NotNull(message = "Category is required")
    private ProductCategoryEnum category;

    public ProductDTO toDto() { 
        return ProductDTO.builder()
                    .itemId(this.itemId)
                    .name(this.name)
                    .description(this.description)
                    .price(this.price)
                    .category(this.category)
                .build();
    }
}
