package com.lanchonete.na.comanda.core.application.usecases.product;

import com.lanchonete.na.comanda.core.application.dto.ProductDTO;
import com.lanchonete.na.comanda.core.domain.product.Product;

public interface UpdateProductUseCase {
    Product updateProduct(ProductDTO product);
}
