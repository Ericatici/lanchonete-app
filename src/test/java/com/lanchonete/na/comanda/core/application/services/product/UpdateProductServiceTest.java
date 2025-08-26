package com.lanchonete.na.comanda.core.application.services.product;

import static com.lanchonete.na.comanda.mocks.dto.ProductDTOMock.productDTOMock;
import static com.lanchonete.na.comanda.mocks.product.ProductMock.productMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lanchonete.na.comanda.core.application.dto.ProductDTO;
import com.lanchonete.na.comanda.core.domain.exeptions.ProductNotFoundException;
import com.lanchonete.na.comanda.core.domain.product.Product;
import com.lanchonete.na.comanda.core.domain.repositories.ProductRepository;

class UpdateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private UpdateProductService updateProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUpdateProductWhenProductExists() {
        final ProductDTO productDTO = productDTOMock();
        final Product existingProduct = productMock();
        final Product updatedProduct = productDTO.toProduct();

        when(productRepository.findProductById(productDTO.getItemId())).thenReturn(existingProduct);
        when(productRepository.updateProduct(any(Product.class))).thenReturn(updatedProduct);

        Product actualResponse = updateProductService.updateProduct(productDTO);

        assertEquals(updatedProduct, actualResponse);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        final ProductDTO productDTO = productDTOMock();
        
        when(productRepository.findProductById(productDTO.getItemId())).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> updateProductService.updateProduct(productDTO));
    }
}
