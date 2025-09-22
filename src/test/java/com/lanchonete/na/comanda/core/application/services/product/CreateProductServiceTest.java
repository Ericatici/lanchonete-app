package com.lanchonete.na.comanda.core.application.services.product;

import static com.lanchonete.na.comanda.mocks.dto.ProductDTOMock.productDTOMock;
import static com.lanchonete.na.comanda.mocks.product.ProductMock.productMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.application.dto.ProductDTO;
import com.lanchonete.na.comanda.core.domain.product.Product;
import com.lanchonete.na.comanda.core.domain.repositories.ProductRepository;

class CreateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CreateProductService createProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProductsWhenInputIsValid() {
        final List<ProductDTO> productDTOs = Collections.singletonList(productDTOMock());
        final List<Product> products = Collections.singletonList(productMock());

        when(productRepository.saveAllProducts(anyList())).thenReturn(products);

        List<Product> actualResponses = createProductService.createProduct(productDTOs);

        assertEquals(products, actualResponses);
    }

    @Test
    void shouldThrowExceptionWhenRepositoryThrowsException() {
        final List<ProductDTO> productDTOs = Collections.singletonList(productDTOMock());

        when(productRepository.saveAllProducts(anyList())).thenThrow(new RuntimeException("DB Error"));

        assertThrows(RuntimeException.class, () -> createProductService.createProduct(productDTOs));
    }
}
