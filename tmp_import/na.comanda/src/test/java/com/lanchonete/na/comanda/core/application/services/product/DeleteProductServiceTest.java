package com.lanchonete.na.comanda.core.application.services.product;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.lanchonete.na.comanda.core.domain.exeptions.ProductDeletionException;
import com.lanchonete.na.comanda.core.domain.exeptions.ProductNotFoundException;
import com.lanchonete.na.comanda.core.domain.repositories.ProductRepository;

class DeleteProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DeleteProductService deleteProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldDeleteProductWhenProductExists() {
        doNothing().when(productRepository).deleteProductById(ITEM_ID);

        deleteProductService.deleteProduct(ITEM_ID);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        doThrow(new EmptyResultDataAccessException(1)).when(productRepository).deleteProductById(ITEM_ID);

        assertThrows(ProductNotFoundException.class, () -> deleteProductService.deleteProduct(ITEM_ID));
    }

    @Test
    void shouldThrowProductDeletionExceptionWhenDataAccessErrorOccurs() {
        doThrow(new DataAccessException("DB Error") {}).when(productRepository).deleteProductById(ITEM_ID);

        assertThrows(ProductDeletionException.class, () -> deleteProductService.deleteProduct(ITEM_ID));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUnexpectedErrorOccurs() {
        doThrow(new RuntimeException("Unexpected Error")).when(productRepository).deleteProductById(ITEM_ID);

        assertThrows(RuntimeException.class, () -> deleteProductService.deleteProduct(ITEM_ID));
    }
}
