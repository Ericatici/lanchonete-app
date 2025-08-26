package com.lanchonete.na.comanda.adapter.driven.persistence.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import static com.lanchonete.na.comanda.adapter.driven.persistence.entities.mappers.ProductEntityMapper.fromProductToProductEntity;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.ProductEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.repositories.jpa.JpaProductRepository;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.product.Product;
import com.lanchonete.na.comanda.core.domain.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository productRepository;

    @Override
    public List<Product> saveAllProducts(List<Product> products) {
        final List<ProductEntity> productEntities = products.stream()
            .map(product -> fromProductToProductEntity(product)).collect(Collectors.toCollection(ArrayList::new));

        final List<ProductEntity> savedProducts = productRepository.saveAll(productEntities);

        return savedProducts.stream().map(ProductEntity::toProduct).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void deleteProductById(String id) {
        productRepository.deleteById(id);  
    }

    @Override
    public List<Product> findAllProducts() {
        List<ProductEntity> foundProducts = productRepository.findAll();

        return foundProducts.stream().map(ProductEntity::toProduct).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Product> findProductsByCategory(ProductCategoryEnum category) {
        List<ProductEntity> foundProducts = productRepository.findProductByCategory(category);

        return foundProducts.stream().map(ProductEntity::toProduct).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Product findProductById(String itemId) {
        Optional<ProductEntity> foundProduct = productRepository.findById(itemId);

        if(foundProduct.isPresent()) {
            return foundProduct.get().toProduct();
        }

        return null;
    }

    @Override
    public Product updateProduct(Product product) {
        final ProductEntity productEntity = fromProductToProductEntity(product);

        final ProductEntity savedProduct = productRepository.save(productEntity);

        return savedProduct.toProduct();
    }
    
}
