package com.lanchonete.na.comanda.adapter.driver.rest.controllers.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.application.dto.OrderDTO.OrderItemDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String customerCpf;

    @Valid
    @NotEmpty(message = "Items list cannot be empty")
    private List<OrderItemRequest> items;

    @Data
    @Builder
    public static class OrderItemRequest {
        @NotEmpty(message = "Product ID is required")
        private String productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 0)
        private Integer quantity;

        public OrderDTO.OrderItemDTO toDto() {
            return OrderItemDTO.builder()
                    .productId(productId)
                    .quantity(quantity)
                .build();
        }
    }

    public OrderDTO toDto() {
        List<OrderItemDTO> itemsDTO = new ArrayList<>();
        if (items != null) {
            itemsDTO = items.stream().map(OrderItemRequest::toDto).collect(Collectors.toCollection(ArrayList::new));
        }
        return OrderDTO.builder()
        .customerCpf(customerCpf)
        .items(itemsDTO)
        .build();
    }

    public OrderDTO toDto(String id) {
        List<OrderItemDTO> itemsDTO = new ArrayList<>();
        if (items != null) {
            itemsDTO = items.stream().map(OrderItemRequest::toDto).collect(Collectors.toCollection(ArrayList::new));
        }
        return OrderDTO.builder()
        .orderId(id)
        .customerCpf(customerCpf)
        .items(itemsDTO)
        .build();
    }
}
