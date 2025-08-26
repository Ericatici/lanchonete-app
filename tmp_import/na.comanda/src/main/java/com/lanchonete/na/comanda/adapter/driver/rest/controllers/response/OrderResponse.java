package com.lanchonete.na.comanda.adapter.driver.rest.controllers.response;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
   
    @JsonProperty
    private Long id;

    @JsonProperty
    private String customerCpf;

    @JsonProperty
    private List<OrderItemResponse> items;

    @JsonProperty
    private LocalDateTime orderDate;

    @JsonProperty
    private OrderStatusEnum status;

    @JsonProperty
    private PaymentStatusEnum paymentStatus;

    @JsonProperty
    private String qrCodeData;

    @JsonProperty
    private BigDecimal totalPrice;

    @JsonProperty
    private String paymentId;

    @JsonProperty
    private Instant createdDate;

    @JsonProperty
    private Instant updatedDate;

    @Data
    @Builder
    public static class OrderItemResponse {
        private String productId;
        private Integer quantity;
        private BigDecimal itemPrice;
    }
}
