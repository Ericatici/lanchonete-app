package com.lanchonete.na.comanda.mocks.product;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_DESCRIPTION;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_NAME;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_PRICE;

import java.time.Instant;

import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.product.Product;

public class ProductMock {

    public static Product productMock(){
        return Product.builder()
                    .itemId(ITEM_ID)
                    .name(ITEM_NAME)
                    .description(ITEM_DESCRIPTION)
                    .price(ITEM_PRICE)
                    .category(ProductCategoryEnum.SNACK)
                .build();
    }

    public static Product productWithDatesMock(){
        return Product.builder()
                    .itemId(ITEM_ID)
                    .name(ITEM_NAME)
                    .description(ITEM_DESCRIPTION)
                    .price(ITEM_PRICE)
                    .category(ProductCategoryEnum.SNACK)
                    .createdDate(Instant.now())
                    .updatedDate(Instant.now())
                .build();
    }
    
}
