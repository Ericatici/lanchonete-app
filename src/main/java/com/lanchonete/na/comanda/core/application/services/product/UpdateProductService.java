package com.lanchonete.na.comanda.core.application.services.product;

import org.springframework.stereotype.Service;

import com.lanchonete.na.comanda.core.application.dto.ProductDTO;
import com.lanchonete.na.comanda.core.application.usecases.product.UpdateProductUseCase;
import com.lanchonete.na.comanda.core.domain.exeptions.ProductNotFoundException;
import com.lanchonete.na.comanda.core.domain.product.Product;
import com.lanchonete.na.comanda.core.domain.repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepository productRepository;
    
    @Override
    @Transactional
    public Product updateProduct(final ProductDTO dto) {
        log.info("Updating product with id: {}", dto.getItemId());

        Product product = productRepository.findProductById(dto.getItemId());

        if (product != null){
            updateExistingProductEntity(product, dto);

            Product updatedProduct = productRepository.updateProduct(product);

            log.info("Product updated successfully with id: {}", updatedProduct.getItemId());

            return updatedProduct;
        }

        log.warn("Product not found with id: {}", dto.getItemId());
        throw new ProductNotFoundException("Product with id " + dto.getItemId() + " not found"); 
    }

    private void updateExistingProductEntity(Product existingProduct, final ProductDTO dto){
        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setCategory(dto.getCategory());
    }
    
}
