package com.lanchonete.na.comanda.adapter.driver.rest.controllers.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class ProductResponse {
    @JsonProperty
    private String itemId;

    @JsonProperty
    private String name;

    @JsonProperty
    private String description;

    @JsonProperty
    private BigDecimal price;

    @JsonProperty
    private ProductCategoryEnum category;

}
