package com.lanchonete.na.comanda.adapter.driven.persistence.entities;

import com.lanchonete.na.comanda.adapter.driven.persistence.entities.common.BaseEntity;
import com.lanchonete.na.comanda.core.domain.customer.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@EqualsAndHashCode(callSuper = false)
public class CustomerEntity extends BaseEntity {
    
    @Id
    @Column(name = "cpf", unique = true)
    private String cpf;
    
    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    public Customer toCustomer() { 
        return Customer.builder()
                .cpf(this.cpf)
                .name(this.name)
                .email(this.email)
                .createdDate(this.getCreatedDate())
                .updatedDate(this.getUpdatedDate())
                .build();
    }

}