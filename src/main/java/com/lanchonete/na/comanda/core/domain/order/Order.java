package com.lanchonete.na.comanda.core.domain.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.lanchonete.na.comanda.adapter.driver.rest.controllers.response.OrderResponse;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class Order {

    private Long id;
    private Customer customer;
    private List<OrderItem> items;
    private LocalDateTime orderDate;
    private OrderStatusEnum status;
    private PaymentStatusEnum paymentStatus;
    private String qrCodeData;
    private String paymentId;
    private BigDecimal totalPrice;
    private Instant createdDate;
    private Instant updatedDate;

    public OrderResponse toOrderResponse() {
        List<OrderResponse.OrderItemResponse> itemResponses = this.items.stream()
                .map(item -> OrderResponse.OrderItemResponse.builder()
                        .productId(item.getProduct().getItemId())
                        .quantity(item.getQuantity())
                        .itemPrice(item.getItemPrice())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));

        String customerCpf = this.customer != null ? this.customer.getCpf() : null;

        return OrderResponse.builder()
                .id(this.getId())
                .customerCpf(customerCpf)
                .items(itemResponses)
                .orderDate(this.getOrderDate())
                .status(this.getStatus())
                .paymentStatus(this.getPaymentStatus())
                .paymentId(this.getPaymentId())
                .qrCodeData(this.getQrCodeData())
                .totalPrice(this.getTotalPrice())
                .createdDate(this.getCreatedDate())
                .updatedDate(this.getUpdatedDate())
                .build();
    }
    
}
