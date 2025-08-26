package com.lanchonete.na.comanda.core.application.usecases.customer;

import com.lanchonete.na.comanda.core.application.dto.CustomerDTO;
import com.lanchonete.na.comanda.core.domain.customer.Customer;

public interface UpdateCustomerUseCase {
    Customer updateCustomer(CustomerDTO customer);
}
