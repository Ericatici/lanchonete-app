package com.lanchonete.na.comanda.core.application.usecases.product;

import java.util.List;

import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.product.Product;

public interface FindProductsUseCase {
    List<Product> getProductsByCategory(ProductCategoryEnum category);
    List<Product> getAllProducts();
    Product findByItemId(String itemId);
}
