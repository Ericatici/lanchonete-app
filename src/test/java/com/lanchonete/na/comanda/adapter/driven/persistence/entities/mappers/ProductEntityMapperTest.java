package com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers;

import static com.lanchonete.na.comanda.mocks.product.ProductMock.productMock;
import static com.lanchonete.na.comanda.mocks.product.ProductMock.productWithDatesMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.ProductEntity;
import com.lanchonete.na.comanda.core.domain.product.Product;

class ProductEntityMapperTest {
    
    
    @Test
    void shouldMapProductToProductEntity() {
        final Product product = productMock();

        final ProductEntity productEntity = ProductEntityMapper.fromProductToProductEntity(product);

        assertEquals(product.getItemId(), productEntity.getItemId());
        assertEquals(product.getName(), productEntity.getName());
        assertEquals(product.getDescription(), productEntity.getDescription());
        assertEquals(product.getPrice(), productEntity.getPrice());
        assertEquals(product.getCategory(), productEntity.getCategory());
        assertNull(productEntity.getCreatedDate());
        assertNull(productEntity.getUpdatedDate());
    }

    @Test
    void shouldMapProductToProductEntityWithDates() {
        final Product product = productWithDatesMock();

        final ProductEntity productEntity = ProductEntityMapper.fromProductToProductEntity(product);

        assertEquals(product.getItemId(), productEntity.getItemId());
        assertEquals(product.getName(), productEntity.getName());
        assertEquals(product.getDescription(), productEntity.getDescription());
        assertEquals(product.getPrice(), productEntity.getPrice());
        assertEquals(product.getCategory(), productEntity.getCategory());
        assertEquals(product.getCreatedDate(), productEntity.getCreatedDate());
        assertEquals(product.getUpdatedDate(), productEntity.getUpdatedDate());
    }
}
