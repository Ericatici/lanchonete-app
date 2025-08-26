package com.lanchonete.na.comanda.adapter.driven.persistence.entities;

import java.math.BigDecimal;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.common.BaseEntity;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@EqualsAndHashCode(callSuper = false)
public class ProductEntity extends BaseEntity {
    
    @Id
    @Column(name = "id", unique = true)
    private String itemId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ProductCategoryEnum category;


    public Product toProduct() { 
        return Product.builder()
                .itemId(this.itemId)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .category(this.category)
                .createdDate(this.getCreatedDate())
                .updatedDate(this.getUpdatedDate())
                .build();
    }
}
