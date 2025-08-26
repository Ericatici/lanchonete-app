package com.lanchonete.na.comanda.adapter.driver.rest.controllers;

import static com.lanchonete.na.comanda.core.application.common.ContextLogger.checkTraceId;
import static  com.lanchonete.na.comanda.core.application.constants.ApiContants.REQUEST_TRACE_ID;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.ProductRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.ProductUpdateRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.ProductResponse;
import com.lanchonete.na.comanda.core.application.dto.ProductDTO;
import com.lanchonete.na.comanda.core.application.usecases.product.CreateProductUseCase;
import com.lanchonete.na.comanda.core.application.usecases.product.DeleteProductUseCase;
import com.lanchonete.na.comanda.core.application.usecases.product.FindProductsUseCase;
import com.lanchonete.na.comanda.core.application.usecases.product.UpdateProductUseCase;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;
import com.lanchonete.na.comanda.core.domain.product.Product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Products", description = "Operations related to products")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final FindProductsUseCase findProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;

     @Operation(summary = "Create new products", description = "Creates a list of products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Products created successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Invalid input", content = @Content)
    })
    @PostMapping(path = "/save")
    public ResponseEntity<List<ProductResponse>> createProduct(@RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId,
        @RequestBody @NotEmpty final List<@Valid ProductRequest> products) {
        checkTraceId(requestTraceId);

        log.info("Received request to create products");

        List<ProductDTO> productDTOList = products.stream().map(ProductRequest::toDto).toList();

        final List<Product> productList = createProductUseCase.createProduct(productDTOList);
        final List<ProductResponse> productResponseList = productList.stream().map(Product::toProductResponse).toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseList);
    }

    @Operation(summary = "Update a product", description = "Updates an existing product by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId, 
        @PathVariable final String id, @Valid @RequestBody final ProductUpdateRequest product) {
        checkTraceId(requestTraceId);

        log.info("Received request to update product with item id: {}", id);

        final Product updatedProduct = updateProductUseCase.updateProduct(product.toDto(id));
        final ProductResponse updatedProductResponse = updatedProduct.toProductResponse();
        return ResponseEntity.ok(updatedProductResponse);
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId, @PathVariable final String id) {
        checkTraceId(requestTraceId);

        log.info("Received request to delete product with id: {}", id);

        deleteProductUseCase.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all products", description = "Retrieves a list of all products.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of products retrieved successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
})
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId) {
        checkTraceId(requestTraceId);

        log.info("Received request to get all products");

        final List<Product> products = findProductUseCase.getAllProducts();
        final List<ProductResponse> productResponses = products.stream().map(Product::toProductResponse).toList();
        return ResponseEntity.ok(productResponses);
    }

    @Operation(summary = "Get products by category", description = "Retrieves a list of products filtered by category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId,
     @PathVariable final ProductCategoryEnum category) {
        checkTraceId(requestTraceId);

        log.info("Received request to get products by category: {}", category);

        final List<Product> products = findProductUseCase.getProductsByCategory(category);
        final List<ProductResponse> productResponses = products.stream().map(Product::toProductResponse).toList();
        return ResponseEntity.ok(productResponses);
    }
}
