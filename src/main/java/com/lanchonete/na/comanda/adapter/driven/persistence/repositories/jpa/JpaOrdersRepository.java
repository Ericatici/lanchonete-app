package com.lanchonete.na.comanda.adapter.driven.persistence.repositories.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.CustomerEntity;
import com.lanchonete.na.comanda.adapter.driven.persistence.entities.OrderEntity;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;

@Repository
public interface JpaOrdersRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByStatus(OrderStatusEnum status);
    List<OrderEntity> findAllByCustomer(CustomerEntity customer);
    Optional<OrderEntity> findByPaymentId(String paymentId);
    List<OrderEntity> findAllByStatusIn(List<OrderStatusEnum> statuses);
}