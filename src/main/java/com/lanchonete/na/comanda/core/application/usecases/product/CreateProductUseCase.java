package com.lanchonete.na.comanda.core.application.usecases.product;

import java.util.List;

import com.lanchonete.na.comanda.core.application.dto.ProductDTO;
import com.lanchonete.na.comanda.core.domain.product.Product;

public interface CreateProductUseCase {
    List<Product> createProduct(List<ProductDTO> products);
}
