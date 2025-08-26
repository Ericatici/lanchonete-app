package com.lanchonete.na.comanda.core.application.usecases.customer;

import java.util.Optional;

import com.lanchonete.na.comanda.core.domain.customer.Customer;

public interface FindCustomerUseCase {
    Optional<Customer> getCustomerOptionalByCpf(String cpf);
    Customer getCustomerByCpf(String cpf);
}
