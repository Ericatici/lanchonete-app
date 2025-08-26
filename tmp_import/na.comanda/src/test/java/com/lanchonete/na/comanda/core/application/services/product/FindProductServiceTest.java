package com.lanchonete.na.comanda.core.application.services.product;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;
import static com.lanchonete.na.comanda.mocks.product.ProductMock.productMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.ProductNotFoundException;
import com.lanchonete.na.comanda.core.domain.product.Product;
import com.lanchonete.na.comanda.core.domain.repositories.ProductRepository;

class FindProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FindProductService findProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnProductsWhenProductsExistInCategory() {
        final ProductCategoryEnum category = ProductCategoryEnum.SNACK;
        final List<Product> products = Collections.singletonList(productMock());

        when(productRepository.findProductsByCategory(category)).thenReturn(products);

        List<Product> actualResponses = findProductService.getProductsByCategory(category);

        assertEquals(products, actualResponses);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenNoProductsExistInCategory() {
        final ProductCategoryEnum category = ProductCategoryEnum.SNACK;

        when(productRepository.findProductsByCategory(category)).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> findProductService.getProductsByCategory(category));
    }

    @Test
    void shouldReturnAllProductsWhenProductsExist() {
        final List<Product> products = Collections.singletonList(productMock());

        when(productRepository.findAllProducts()).thenReturn(products);

        List<Product> actualResponses = findProductService.getAllProducts();

        assertEquals(products, actualResponses);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenNoProductsExist() {
        when(productRepository.findAllProducts()).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> findProductService.getAllProducts());
    }

    @Test
    void shouldReturnProductWhenProductExists() {
        final Product product = productMock();

        when(productRepository.findProductById(ITEM_ID)).thenReturn(product);

        Product actualEntity = findProductService.findByItemId(ITEM_ID);

        assertEquals(product, actualEntity);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        when(productRepository.findProductById(ITEM_ID)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> findProductService.findByItemId(ITEM_ID));
    }
}