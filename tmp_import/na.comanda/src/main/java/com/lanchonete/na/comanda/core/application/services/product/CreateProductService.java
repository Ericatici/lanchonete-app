package com.lanchonete.na.comanda.core.application.services.product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lanchonete.na.comanda.core.application.dto.ProductDTO;
import com.lanchonete.na.comanda.core.application.usecases.product.CreateProductUseCase;
import com.lanchonete.na.comanda.core.domain.product.Product;
import com.lanchonete.na.comanda.core.domain.repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CreateProductService implements CreateProductUseCase {
    
    private final ProductRepository productRepository;
    
    @Override
    @Transactional
    public List<Product> createProduct(final List<ProductDTO> dtos) {
        log.info("Creating {} products", dtos.size());

        try {
            List<Product> products = dtos.stream().map(ProductDTO::toProduct).collect(Collectors.toCollection(ArrayList::new));

            List<Product> savedProducts = productRepository.saveAllProducts(products);

            log.info("Successfully created {} products", savedProducts.size());
            return savedProducts;

        } catch (Exception e) {
            log.error("Error creating products: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create products", e); 
        }
    }


    
    
}
