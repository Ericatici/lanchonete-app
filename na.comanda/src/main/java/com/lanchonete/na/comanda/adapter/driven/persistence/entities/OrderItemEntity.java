package com.lanchonete.na.comanda.adapter.driven.persistence.entities;

import java.math.BigDecimal;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.common.BaseEntity;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "order_items")
@EqualsAndHashCode(callSuper = false)
public class OrderItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "item_price")
    private BigDecimal itemPrice;

    public OrderItem toOrderItem() {
        return OrderItem.builder()
                        .id(this.getId())
                        .product(this.getProduct().toProduct())
                        .quantity(this.getQuantity())
                        .itemPrice(this.getItemPrice())
                        .createdDate(this.getCreatedDate())
                        .updatedDate(this.getUpdatedDate())
                        .build();
    }
}
