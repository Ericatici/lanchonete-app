package com.lanchonete.na.comanda.adapter.driven.persistence.repositories;

import static com.lanchonete.na.comanda.mocks.product.ProductMock.productMock;
import static com.lanchonete.na.comanda.mocks.entity.ProductEntityMock.productEntityMock;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.ProductEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.repositories.jpa.JpaProductRepository;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.product.Product;

class ProductRepositoryImplTest {
    
    @Mock
    private JpaProductRepository jpaProductRepository;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveAllProducts() {
        final Product product = productMock(); 
        final List<Product> products = Collections.singletonList(product);
        final ProductEntity productEntity = productEntityMock();
        final List<ProductEntity> productEntities = Collections.singletonList(productEntity);

        when(jpaProductRepository.saveAll(anyList())).thenReturn(productEntities);

        List<Product> savedProducts = productRepository.saveAllProducts(products);

        assertNotNull(savedProducts);
        assertEquals(1, savedProducts.size());
        assertEquals(product.getItemId(), savedProducts.get(0).getItemId());
        verify(jpaProductRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldDeleteProductById() {
        doNothing().when(jpaProductRepository).deleteById(anyString());

        productRepository.deleteProductById(ITEM_ID);

        verify(jpaProductRepository, times(1)).deleteById(ITEM_ID);
    }

    @Test
    void shouldFindAllProducts() {
        final Product product = productMock(); 
        final ProductEntity productEntity = productEntityMock();
        final List<ProductEntity> productEntities = Collections.singletonList(productEntity);

        when(jpaProductRepository.findAll()).thenReturn(productEntities);

        List<Product> foundProducts = productRepository.findAllProducts();

        assertNotNull(foundProducts);
        assertEquals(1, foundProducts.size());
        assertEquals(product.getItemId(), foundProducts.get(0).getItemId());
        verify(jpaProductRepository, times(1)).findAll();
    }

    @Test
    void shouldFindProductsByCategory() {
        final ProductCategoryEnum category = ProductCategoryEnum.SNACK;
        final Product product = productMock();
        final ProductEntity productEntity = productEntityMock();
        final List<ProductEntity> productEntities = Collections.singletonList(productEntity);

        when(jpaProductRepository.findProductByCategory(category)).thenReturn(productEntities);

        List<Product> foundProducts = productRepository.findProductsByCategory(category);

        assertNotNull(foundProducts);
        assertEquals(1, foundProducts.size());
        assertEquals(product.getCategory(), foundProducts.get(0).getCategory());
        verify(jpaProductRepository, times(1)).findProductByCategory(category);
    }

    @Test
    void shouldFindProductByIdWhenPresent() {
        final Product product = productMock(); 
        final ProductEntity productEntity = productEntityMock();

        when(jpaProductRepository.findById(ITEM_ID)).thenReturn(Optional.of(productEntity));

        Product foundProduct = productRepository.findProductById(ITEM_ID);

        assertNotNull(foundProduct);
        assertEquals(product.getItemId(), foundProduct.getItemId());
        verify(jpaProductRepository, times(1)).findById(ITEM_ID);
    }

    @Test
    void shouldReturnNullWhenProductNotFoundById() {
        when(jpaProductRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

        Product foundProduct = productRepository.findProductById(ITEM_ID);

        assertNull(foundProduct);
        verify(jpaProductRepository, times(1)).findById(ITEM_ID);
    }

    @Test
    void shouldUpdateProduct() {
        final Product product = productMock(); 
        final ProductEntity productEntity = productEntityMock();

        when(jpaProductRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        Product updatedProduct = productRepository.updateProduct(product);

        assertNotNull(updatedProduct);
        assertEquals(product.getItemId(), updatedProduct.getItemId());
        verify(jpaProductRepository, times(1)).save(any(ProductEntity.class));
    }
}
