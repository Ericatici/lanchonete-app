package com.lanchonete.na.comanda.mocks.entity;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_DESCRIPTION;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_NAME;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_PRICE;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.ProductEntity;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;

public class ProductEntityMock {
    
    public static ProductEntity productEntityMock(){
        return ProductEntity.builder()
                    .itemId(ITEM_ID)
                    .name(ITEM_NAME)
                    .description(ITEM_DESCRIPTION)
                    .price(ITEM_PRICE)
                    .category(ProductCategoryEnum.SNACK)
                .build();
    }
}
