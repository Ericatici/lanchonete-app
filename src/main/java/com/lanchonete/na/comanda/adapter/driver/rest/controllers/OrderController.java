package com.lanchonete.na.comanda.adapter.driver.rest.controllers;

import static com.lanchonete.na.comanda.core.application.common.ContextLogger.checkTraceId;
import static  com.lanchonete.na.comanda.core.application.constants.ApiContants.REQUEST_TRACE_ID;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests.OrderRequest;
import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.OrderResponse;
import com.lanchonete.na.comanda.core.application.usecases.order.CreateOrderUseCase;
import com.lanchonete.na.comanda.core.application.usecases.order.FindOrdersUseCase;
import com.lanchonete.na.comanda.core.application.usecases.order.UpdateOrderUseCase;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Orders", description = "Operations related to orders")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final FindOrdersUseCase findOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;

    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(
            @RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId,
            @Valid @RequestBody final OrderRequest orderRequest) {
        checkTraceId(requestTraceId);

        log.info("Received request for checkout: {}", orderRequest);
        final Order createdOrder = createOrderUseCase.createOrder(orderRequest.toDto());
        final OrderResponse createdOrderResponse = createdOrder.toOrderResponse();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderResponse);
    }

    @Operation(summary = "List all orders")
    @ApiResponse(responseCode = "200", description = "List of orders retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class))))
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId) {
        checkTraceId(requestTraceId);

        log.info("Received request to list all orders");
        final List<Order> orders = findOrderUseCase.getAllOrders();
        final List<OrderResponse> ordersResponse = orders.stream().map(Order::toOrderResponse).toList();
        
        return ResponseEntity.ok(ordersResponse);
    }

    @Operation(summary = "List orders by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(
            @RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId,
            @PathVariable final OrderStatusEnum status) {
        checkTraceId(requestTraceId);

        log.info("Received request to list orders by status: {}", status);
        final List<Order> orders = findOrderUseCase.getAllOrdersByStatus(status);
        final List<OrderResponse> ordersResponse = orders.stream().map(Order::toOrderResponse).toList();
        
        return ResponseEntity.ok(ordersResponse);
    }

    @Operation(summary = "Get order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId,
            @PathVariable final Long id) {
        checkTraceId(requestTraceId);

        log.info("Received request to get order by id: {}", id);
        final Order order = findOrderUseCase.getOrderById(id);
        final OrderResponse ordersResponse = order.toOrderResponse();

        return ResponseEntity.ok(ordersResponse);
    }

    @Operation(summary = "Update order status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid status")
    })
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId,
            @PathVariable final Long id,
            @PathVariable final OrderStatusEnum status) {
        checkTraceId(requestTraceId);

        log.info("Received request to update order {} status to {}", id, status);
        final Order updatedOrder = updateOrderUseCase.updateOrderStatusById(id, status);
        final OrderResponse updatedOrderResponse = updatedOrder.toOrderResponse();
        
        return ResponseEntity.ok(updatedOrderResponse);
    }

    @Operation(summary = "Update order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId,
            @PathVariable final Long id,
            @Valid @RequestBody final OrderRequest orderRequest) {
        checkTraceId(requestTraceId);

        log.info("Received request to update order {}", id);
        final Order updatedOrder = updateOrderUseCase.updateOrder(id, orderRequest.toDto());
        final OrderResponse updatedOrderResponse = updatedOrder.toOrderResponse();
        
        return ResponseEntity.ok(updatedOrderResponse);
    }


    @Operation(summary = "Get all active orders with descriptions, sorted by status and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of active orders retrieved successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "204", description = "No active orders found")
    })
    @GetMapping("/activeOrders")
    public ResponseEntity<List<OrderResponse>> getAllActiveOrders(
            @RequestHeader(value = REQUEST_TRACE_ID, required = false) String requestTraceId) {
        checkTraceId(requestTraceId);

        log.info("Received request to get all active orders");
        final List<Order> orders = findOrderUseCase.getAllActiveOrdersSorted(); 
        final List<OrderResponse> orderResponses = orders.stream().map(Order::toOrderResponse).toList();

        if (orderResponses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(orderResponses);
    }

}
