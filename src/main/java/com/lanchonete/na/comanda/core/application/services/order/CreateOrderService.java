package com.lanchonete.na.comanda.core.application.services.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.application.usecases.customer.FindCustomerUseCase;
import com.lanchonete.na.comanda.core.application.usecases.order.CreateOrderItemUseCase;
import com.lanchonete.na.comanda.core.application.usecases.order.CreateOrderUseCase;
import com.lanchonete.na.comanda.core.application.usecases.payment.GeneratePaymentQrCodeUseCase;
import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.enums.PaymentStatusEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.InvalidOrderException;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepository ordersRepository;
    private final FindCustomerUseCase findCustomerUseCase;
    private final CreateOrderItemUseCase createOrderItemUseCase;
    private final GeneratePaymentQrCodeUseCase generatePaymentQrCodeUseCase;
    
    @Override
    public Order createOrder(final OrderDTO orderDTO) {
      
        Customer customer = null;
        if (StringUtils.isNotBlank(orderDTO.getCustomerCpf())) {
            customer = findCustomerUseCase.getCustomerByCpf(orderDTO.getCustomerCpf()); 
        }

        if (CollectionUtils.isEmpty(orderDTO.getItems())){
            throw new InvalidOrderException("Order with no items, order is invalid");
        }
   
        Order order = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .status(OrderStatusEnum.WAITING_PAYMENT)
                .paymentStatus(PaymentStatusEnum.PENDING)
                .items(new ArrayList<>())
                .build();
     
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderDTO.OrderItemDTO itemDTO : orderDTO.getItems()) {
            OrderItem orderItem = createOrderItemUseCase.createOrderItem(order, itemDTO);
            order.getItems().add(orderItem);
            totalPrice = totalPrice.add(orderItem.getItemPrice()); 
        }

        order.setTotalPrice(totalPrice);

        final Order savedOrder = ordersRepository.saveOrder(order);

        final Order paymentUpdatedOrder = generatePaymentQrCodeUseCase.generatePaymentQrCode(savedOrder);

        final Order updatedOrder = ordersRepository.updateOrder(paymentUpdatedOrder);

        log.info("Created order with id: {}", updatedOrder.getId());

        return updatedOrder;
    }
}
