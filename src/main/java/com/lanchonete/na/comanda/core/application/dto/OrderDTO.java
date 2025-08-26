package com.lanchonete.na.comanda.core.application.dto;

import java.util.List;

import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO {
    private String orderId;
    private String customerCpf;
    private List<OrderItemDTO> items;
    private OrderStatusEnum status;

    @Data
    @Builder
    public static class OrderItemDTO {
        private String productId;
        private Integer quantity;
    }

}
