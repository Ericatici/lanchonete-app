package com.lanchonete.na.comanda.core.application.services.product;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.lanchonete.na.comanda.core.application.usecases.product.DeleteProductUseCase;
import com.lanchonete.na.comanda.core.domain.exeptions.ProductDeletionException;
import com.lanchonete.na.comanda.core.domain.exeptions.ProductNotFoundException;
import com.lanchonete.na.comanda.core.domain.repositories.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class DeleteProductService implements DeleteProductUseCase {
    
    private final ProductRepository productRepository;
    
    @Override
    public void deleteProduct(final String id) {
        log.info("Deleting product with id {}", id);

        try {
            productRepository.deleteProductById(id);   
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException("Product with id " + id + " not found", e);
        } catch (DataAccessException e) {
            throw new ProductDeletionException("Error deleting product with id " + id, e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while deleting product with id " + id, e);
        }
    }
    
}
