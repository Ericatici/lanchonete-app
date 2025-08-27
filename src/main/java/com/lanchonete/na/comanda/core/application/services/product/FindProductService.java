package com.lanchonete.na.comanda.core.application.services.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.lanchonete.na.comanda.core.application.usecases.product.FindProductsUseCase;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.ProductNotFoundException;
import com.lanchonete.na.comanda.core.domain.product.Product;
import com.lanchonete.na.comanda.core.domain.repositories.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class FindProductService implements FindProductsUseCase {

    private final ProductRepository productRepository;
    
    @Override
    public List<Product> getProductsByCategory(final ProductCategoryEnum category) {
        log.info("Finding products with Category: {}", category);
    
        List<Product> products = productRepository.findProductsByCategory(category);

        if (CollectionUtils.isEmpty(products)) {
            log.warn("No products found with category: {}", category);
            throw new ProductNotFoundException("No products found with category: " + category);
        }

        return products;
    }

    @Override
    public List<Product> getAllProducts() {
        log.info("Finding all products");

        List<Product> foundProducts = productRepository.findAllProducts();

        if (CollectionUtils.isEmpty(foundProducts)) {
            log.warn("No products found");
            throw new ProductNotFoundException("No products found");
        }

        return foundProducts;
    }

    @Override
    public Product findByItemId(final String itemId) {
        log.info("Finding product by item id");

        Product product = productRepository.findProductById(itemId);

        if (product == null) {
            throw new ProductNotFoundException("Product with itemId " + itemId + " not found");
        }

        return product;
    }
    
}
