package com.lanchonete.na.comanda.core.domain.repositories;

import java.util.List;

import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.product.Product;

public interface ProductRepository {

    List<Product> saveAllProducts (List<Product> products);
    void deleteProductById (String id);
    List<Product> findAllProducts ();
    List<Product> findProductsByCategory (ProductCategoryEnum category);
    Product findProductById (String id);
    Product updateProduct (Product product);
    
}
