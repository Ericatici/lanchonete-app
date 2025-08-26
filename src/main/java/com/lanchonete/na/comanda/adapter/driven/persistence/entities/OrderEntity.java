package com.lanchonete.na.comanda.adapter.driven.persistence.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.common.BaseEntity;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@EqualsAndHashCode(callSuper = false)
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_cpf")
    private CustomerEntity customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @Column(name = "payment_status") 
    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum paymentStatus;

    @Column(name = "qr_code_data") 
    private String qrCodeData;

    @Column(name = "payment_id") 
    private String paymentId;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public Order toOrder() {
        List<OrderItem> itemList = this.getItems().stream()
                .map(OrderItemEntity::toOrderItem)
                .collect(Collectors.toCollection(ArrayList::new));

        return Order.builder()
                .id(this.getId())
                .customer(this.getCustomer() != null ? this.getCustomer().toCustomer() : null)
                .items(itemList)
                .orderDate(this.getOrderDate())
                .status(this.getStatus())
                .paymentStatus(this.getPaymentStatus())
                .qrCodeData(this.getQrCodeData())
                .paymentId(this.getPaymentId())
                .totalPrice(this.getTotalPrice())
                .createdDate(this.getCreatedDate())
                .updatedDate(this.getUpdatedDate())
                .build();
    }
}