package com.lanchonete.na.comanda.mocks.request;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_DESCRIPTION;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_NAME;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_PRICE;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.ProductUpdateRequest;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;

public class ProductUpdateRequestMock {
    
    public static ProductUpdateRequest productUpdateRequestMock(){
        return ProductUpdateRequest.builder()
                    .name(ITEM_NAME)
                    .description(ITEM_DESCRIPTION)
                    .price(ITEM_PRICE)
                    .category(ProductCategoryEnum.SNACK)
                .build();
    }

}
