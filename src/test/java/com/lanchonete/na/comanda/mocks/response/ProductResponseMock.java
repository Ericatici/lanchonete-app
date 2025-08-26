package com.lanchonete.na.comanda.mocks.response;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_DESCRIPTION;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_NAME;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_PRICE;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.ProductResponse;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;

public class ProductResponseMock {

    public static ProductResponse productResponseMock(){
        return ProductResponse.builder()
                    .itemId(ITEM_ID)
                    .name(ITEM_NAME)
                    .description(ITEM_DESCRIPTION)
                    .price(ITEM_PRICE)
                    .category(ProductCategoryEnum.SNACK)
                .build();
    }
    
}
