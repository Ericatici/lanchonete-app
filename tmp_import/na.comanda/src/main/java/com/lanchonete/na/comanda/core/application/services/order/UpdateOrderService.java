package com.lanchonete.na.comanda.core.application.services.order;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lanchonete.na.comanda.core.domain.customer.Customer;
import com.lanchonete.na.comanda.core.application.dto.OrderDTO;
import com.lanchonete.na.comanda.core.application.dto.OrderDTO.OrderItemDTO;
import com.lanchonete.na.comanda.core.application.usecases.customer.FindCustomerUseCase;
import com.lanchonete.na.comanda.core.application.usecases.order.CreateOrderItemUseCase;
import com.lanchonete.na.comanda.core.application.usecases.order.UpdateOrderUseCase;
import com.lanchonete.na.comanda.core.domain.enums.OrderStatusEnum;
import com.lanchonete.na.comanda.core.domain.exeptions.InvalidOrderException;
import com.lanchonete.na.comanda.core.domain.exeptions.OrderNotFoundException;
import com.lanchonete.na.comanda.core.domain.order.Order;
import com.lanchonete.na.comanda.core.domain.order.OrderItem;
import com.lanchonete.na.comanda.core.domain.repositories.OrderRepository;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateOrderService implements UpdateOrderUseCase {

    private final OrderRepository ordersRepository;
    private final FindCustomerUseCase findCustomerUseCase;
    private final CreateOrderItemUseCase createOrderItemUseCase;

    @Override
    @Transactional
    public Order updateOrderStatusById(final Long orderId, final OrderStatusEnum status) {
        log.info("Updating order with id: {}", orderId);

        Order order = ordersRepository.findOrderById(orderId);

        if (order != null){            
            if (!status.equals(order.getStatus())) {

                validateOrderStatusTransition(order.getStatus(), status);

                order.setStatus(status);
                Order updatedProduct = ordersRepository.updateOrder(order);

                log.info("Order updated successfully with id: {}", orderId);

                return updatedProduct;
            } else {
                log.warn("Order with id {} already has status {}", orderId, status);
                return order;
            }
            
        }

        log.error("Order not found with id: {}", orderId);
        throw new OrderNotFoundException("Order with id " + orderId + " not found"); 
    }

    private void validateOrderStatusTransition(OrderStatusEnum currentStatus, OrderStatusEnum newStatus) {
        if (Objects.equals(currentStatus, newStatus)) {
            return;
        }

        List<OrderStatusEnum> validTransitions = switch (currentStatus) {
            case WAITING_PAYMENT -> Arrays.asList(OrderStatusEnum.RECEIVED, OrderStatusEnum.CANCELLED);
            case RECEIVED -> Arrays.asList(OrderStatusEnum.IN_PREPARATION, OrderStatusEnum.CANCELLED);
            case IN_PREPARATION -> Arrays.asList(OrderStatusEnum.READY, OrderStatusEnum.CANCELLED);
            case READY -> Arrays.asList(OrderStatusEnum.FINISHED); 
            case FINISHED, CANCELLED -> List.of();
            default -> List.of();
        };

        if (!validTransitions.contains(newStatus)) {
            throw new InvalidOrderException(String.format("Invalid order status transition from %s to %s", currentStatus, newStatus));
        }

        if (newStatus.ordinal() < currentStatus.ordinal() && newStatus != OrderStatusEnum.CANCELLED) {
            throw new InvalidOrderException(String.format("Cannot transition order status from %s to a previous status %s", currentStatus, newStatus));
        }
    }


    @Override
    @Transactional
    public Order updateOrder(final Long id, final OrderDTO orderDTO) {
        Order existingOrder = ordersRepository.findOrderById(id);

        if (existingOrder == null) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }

        if (!hasOrderChanged(existingOrder, orderDTO)) {
            log.warn("Nothing to be updated in the Order with id {} ", id);
            return existingOrder; 
        } 
                
        updateOrderEntity(existingOrder, orderDTO);
        
        Order savedOrder = ordersRepository.updateOrder(existingOrder);
        return savedOrder;
    }

    private boolean hasOrderChanged(Order existingOrder, final OrderDTO orderDTO) {
        if (!Objects.equals(existingOrder.getCustomer().getCpf(), orderDTO.getCustomerCpf())) {
            return true;
        }
    
        if (existingOrder.getItems().size() != orderDTO.getItems().size()) {
            return true;
        }

        Map<String, Integer> orderItemDTOMap = orderDTO.getItems().stream()
            .collect(Collectors.toMap(OrderItemDTO::getProductId, OrderItemDTO::getQuantity, Integer::sum));

        Map<String, Integer> existingOrderItemMap = existingOrder.getItems().stream()
            .collect(Collectors.toMap(OrderItem::getItemId, OrderItem::getQuantity, Integer::sum));

        if (!orderItemDTOMap.equals(existingOrderItemMap)){
            return true;
        }
        
        return false;
    }

    private void updateOrderEntity(Order existingOrder, final OrderDTO orderDTO) {
        Customer customer = existingOrder.getCustomer();
        if (StringUtils.isNotBlank(orderDTO.getCustomerCpf())) {
            customer = findCustomerUseCase.getCustomerByCpf(orderDTO.getCustomerCpf()); 
        }

        existingOrder.setCustomer(customer);

        existingOrder.getItems().clear();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderDTO.OrderItemDTO itemDTO : orderDTO.getItems()) {
            OrderItem orderItem = createOrderItemUseCase.createOrderItem(existingOrder, itemDTO);
            existingOrder.getItems().add(orderItem);
            totalPrice.add(orderItem.getItemPrice());
        }
     
        existingOrder.setTotalPrice(totalPrice);
    }

    @Override
    public Order updateOrder(final Order order) {
        final Order savedOrder = ordersRepository.updateOrder(order);
        return savedOrder;
    }
    
}
