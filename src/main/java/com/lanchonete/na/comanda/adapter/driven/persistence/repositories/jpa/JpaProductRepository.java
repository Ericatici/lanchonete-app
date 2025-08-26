package com.lanchonete.na.comanda.adapter.driven.persistence.repositories.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.ProductEntity;
import com.lanchonete.na.comanda.core.domain.enums.ProductCategoryEnum;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, String> {
    List<ProductEntity> findProductByCategory (ProductCategoryEnum category);
}


