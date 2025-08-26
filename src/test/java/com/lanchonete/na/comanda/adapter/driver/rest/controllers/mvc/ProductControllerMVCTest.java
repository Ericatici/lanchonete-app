package com.lanchonete.na.comanda.adapter.driver.rest.controllers.mvc;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_NAME;
import static com.lanchonete.na.comanda.mocks.request.ProductRequestMock.productRequestMock;
import static com.lanchonete.na.comanda.mocks.request.ProductUpdateRequestMock.productUpdateRequestMock;
import static com.lanchonete.na.comanda.mocks.product.ProductMock.productMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.ProductController;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.ProductRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.ProductUpdateRequest;
import com.lanchonete.na.comanda.core.application.services.product.CreateProductService;
import com.lanchonete.na.comanda.core.application.services.product.DeleteProductService;
import com.lanchonete.na.comanda.core.application.services.product.FindProductService;
import com.lanchonete.na.comanda.core.application.services.product.UpdateProductService;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.ProductNotFoundException;
import com.lanchonete.na.comanda.core.domain.product.Product;

@WebMvcTest(ProductController.class)
public class ProductControllerMVCTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateProductService createProductService;

    @MockitoBean
    private DeleteProductService deleteProductService;

    @MockitoBean
    private FindProductService findProductService;

    @MockitoBean
    private UpdateProductService updateProductService;


    @Test
    void shouldCreateProductsSuccessfullyWhenInputIsValid() throws Exception {
        List<ProductRequest> productRequests = Collections.singletonList(productRequestMock());
        List<Product> products = Collections.singletonList(productMock());

        when(createProductService.createProduct(anyList())).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequests)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].itemId").value(ITEM_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(ITEM_NAME));
    }

    @Test
    void shouldReturnBadRequestWhenCreateProductInputIsInvalid() throws Exception {
        ProductRequest invalidRequest = ProductRequest.builder().build(); 
        List<ProductRequest> productRequests = Collections.singletonList(invalidRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequests)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void shouldReturnBadRequestWhenCreateProductInputIsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/product/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ArrayList<>())))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void shouldUpdateProductSuccessfullyWhenInputIsValid() throws Exception {
        when(updateProductService.updateProduct(any())).thenReturn(productMock());

        mockMvc.perform(MockMvcRequestBuilders.put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateRequestMock())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(ITEM_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ITEM_NAME));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateProductInputIsInvalid() throws Exception {
        ProductUpdateRequest invalidRequest = ProductUpdateRequest.builder().build();

        mockMvc.perform(MockMvcRequestBuilders.put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenUpdateProductNotFound() throws Exception {
        when(updateProductService.updateProduct(any())).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/product/999")  
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateRequestMock())))
                .andExpect(MockMvcResultMatchers.status().isNotFound()); 
    }

    @Test
    void shouldDeleteProductSuccessfullyWhenProductExists() throws Exception {
        doNothing().when(deleteProductService).deleteProduct(ITEM_ID);

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeleteProductNotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found")).when(deleteProductService).deleteProduct("999");

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()); 
    }

    @Test
    void shouldGetAllProductsSuccessfullyWhenProductsExist() throws Exception {
        List<Product> products = Collections.singletonList(productMock());

        when(findProductService.getAllProducts()).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].itemId").value(ITEM_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(ITEM_NAME));
    }

    @Test
    void shouldReturnNotFoundWhenNoProductsExist() throws Exception {
        doThrow(new ProductNotFoundException("Product not found")).when(findProductService).getAllProducts();

        mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()); 
    }

    @Test
    void shouldGetProductsByCategorySuccessfullyWhenCategoryExists() throws Exception {
        List<Product> products = Collections.singletonList(productMock());
        when(findProductService.getProductsByCategory(ProductCategoryEnum.SNACK)).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/product/category/SNACK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].itemId").value(ITEM_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value("SNACK"));
    }

    @Test
    void shouldReturnNotFoundWhenProductsByCategoryNotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found")).when(findProductService).getProductsByCategory(ProductCategoryEnum.DRINK);

        mockMvc.perform(MockMvcRequestBuilders.get("/product/category/DRINK"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()); 
    }
}
