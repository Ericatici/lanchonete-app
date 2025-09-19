package com.lanchonete.na.comanda.adapter.driver.rest.controllers;

import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ITEM_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.REQUEST_TRACE_ID;
import static com.lanchonete.na.comanda.mocks.request.ProductRequestMock.productRequestMock;
import static com.lanchonete.na.comanda.mocks.request.ProductUpdateRequestMock.productUpdateRequestMock;
import static com.lanchonete.na.comanda.mocks.response.ProductResponseMock.productResponseMock;
import static com.lanchonete.na.comanda.mocks.product.ProductMock.productMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.ProductRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.ProductUpdateRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.ProductResponse;
import com.lanchonete.na.comanda.core.application.services.product.CreateProductService;
import com.lanchonete.na.comanda.core.application.services.product.DeleteProductService;
import com.lanchonete.na.comanda.core.application.services.product.FindProductService;
import com.lanchonete.na.comanda.core.application.services.product.UpdateProductService;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.ProductNotFoundException;
import com.lanchonete.na.comanda.core.domain.product.Product;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

class ProductControllerTest {

    @Mock
    private CreateProductService createProductService;

    @Mock
    private FindProductService findProductService;

    @Mock
    private UpdateProductService updateProductService;

    @Mock
    private DeleteProductService deleteProductService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void shouldCreateProductsWhenRequestIsValid() {
        final List<ProductRequest> productRequests = Collections.singletonList(productRequestMock());
        final List<ProductResponse> productResponses = Collections.singletonList(productResponseMock());
        final List<Product> products = Collections.singletonList(productMock());

        when(createProductService.createProduct(any())).thenReturn(products);

        ResponseEntity<List<ProductResponse>> response = productController.createProduct(REQUEST_TRACE_ID, productRequests);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productResponses, response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenCreateProductRequestIsInvalid() {
        final  List<ProductRequest> invalidRequests = Collections.singletonList(new ProductRequest("", "", "", BigDecimal.valueOf(-1.0), null));
        final Set<ConstraintViolation<ProductRequest>> violations = new HashSet<>();

        when(createProductService.createProduct(any())).thenThrow(new ConstraintViolationException(violations));

        assertThrows(ConstraintViolationException.class, () -> productController.createProduct(REQUEST_TRACE_ID, invalidRequests));
    }

    @Test
    void shouldUpdateProductWhenRequestIsValid() {
        final ProductUpdateRequest updateRequest = productUpdateRequestMock();
        final ProductResponse updatedResponse = productResponseMock();
        final Product product = productMock();

        when(updateProductService.updateProduct(any())).thenReturn(product);

        ResponseEntity<ProductResponse> response = productController.updateProduct(REQUEST_TRACE_ID, ITEM_ID, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedResponse, response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenUpdateProductRequestIsInvalid() {
        final ProductUpdateRequest invalidRequest = new ProductUpdateRequest("", "", BigDecimal.valueOf(-1.0), null);
        final Set<ConstraintViolation<ProductUpdateRequest>> violations = new HashSet<>();
        
        when(updateProductService.updateProduct(any())).thenThrow(new ConstraintViolationException(violations));

        assertThrows(ConstraintViolationException.class, () -> productController.updateProduct(REQUEST_TRACE_ID, ITEM_ID, invalidRequest));
    }

    @Test
    void shouldDeleteProductWhenProductExists() {       
        doNothing().when(deleteProductService).deleteProduct(ITEM_ID);

        ResponseEntity<Void> response = productController.deleteProduct(REQUEST_TRACE_ID, ITEM_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldGetAllProductsWhenProductsExist() {
        final List<ProductResponse> productResponses = Collections.singletonList(productResponseMock());
        final List<Product> products = Collections.singletonList(productMock());

        when(findProductService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts(REQUEST_TRACE_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productResponses, response.getBody());
    }

    @Test
    void shouldReturnNoContentWhenNoProductsExist() {
        when(findProductService.getAllProducts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts(REQUEST_TRACE_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldGetProductsByCategoryWhenProductsExistInCategory() {
        final ProductCategoryEnum category = ProductCategoryEnum.SNACK;
        final List<ProductResponse> productResponses = Collections.singletonList(productResponseMock());
        final List<Product> products = Collections.singletonList(productMock());
        
        when(findProductService.getProductsByCategory(category)).thenReturn(products);

        ResponseEntity<List<ProductResponse>> response = productController.getProductsByCategory(REQUEST_TRACE_ID, category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productResponses, response.getBody());
    }

    @Test
    void shouldReturnOkWhenNoProductsExistInCategory() {
        final ProductCategoryEnum category = ProductCategoryEnum.SNACK;

        when(findProductService.getProductsByCategory(category)).thenReturn(Collections.emptyList());

        ResponseEntity<List<ProductResponse>> response = productController.getProductsByCategory(REQUEST_TRACE_ID, category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundWhenNoProductsException() {
        final ProductCategoryEnum category = ProductCategoryEnum.SNACK;

        when(findProductService.getProductsByCategory(category)).thenThrow(new ProductNotFoundException("Product Not Found"));

        assertThrows(ProductNotFoundException.class, () -> productController.getProductsByCategory(REQUEST_TRACE_ID, category));
    }
}
