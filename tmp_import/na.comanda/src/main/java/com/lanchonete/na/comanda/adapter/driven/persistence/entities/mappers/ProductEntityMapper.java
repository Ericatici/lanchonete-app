package com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.ProductEntity;
import com.lanchonete.na.comanda.core.domain.product.Product;

public class ProductEntityMapper {

    public static ProductEntity fromProductToProductEntity(final Product product) { 
        ProductEntity productEntity = ProductEntity.builder()
                .itemId(product.getItemId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .build();

        if (product.getCreatedDate() != null) {
            productEntity.setCreatedDate(product.getCreatedDate());
        }

        if (product.getUpdatedDate() != null) {
            productEntity.setUpdatedDate(product.getUpdatedDate());
        }

        return productEntity;
    }
    
}
