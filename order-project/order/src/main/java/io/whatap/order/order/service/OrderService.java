package io.whatap.order.order.service;

import io.whatap.order.event.FailedOrderCreationEvent;
import io.whatap.order.order.controller.req.OrderProductCommand;
import io.whatap.order.order.controller.req.CreateOrderProductCommand;
import io.whatap.order.order.controller.res.OrderDetailDto;
import io.whatap.order.order.entity.Order;
import io.whatap.order.order.entity.OrderProduct;
import io.whatap.order.order.event.CreatedOrderEvent;
import io.whatap.order.order.proxy.ProductProxy;
import io.whatap.order.order.proxy.req.DecreaseInventoryQuantityRequest;
import io.whatap.order.order.proxy.res.ProductDto;
import io.whatap.order.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Value("${spring.application.name:order}")
    private String applicationName;
    private final OrderRepository orderRepository;
    private final ProductProxy productProxy;
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Long orderProduct(OrderProductCommand command) {
        List<DecreaseInventoryQuantityRequest> requests = command.getProducts().stream().map(DecreaseInventoryQuantityRequest::from).toList();
        productProxy.decreaseInventoryQuantities(requests);
        try {
            Order order = Order.from(command);
            addOrderProducts(order, command.getProducts());
            orderRepository.save(order);
            eventPublisher.publishEvent(CreatedOrderEvent.from(order));
            return order.getId();
        } catch (Exception e) {
            kafkaTemplate.send(applicationName,
                    FailedOrderCreationEvent.builder()
                            .orderProducts(command.getProducts().stream()
                                    .map(value -> new FailedOrderCreationEvent.OrderProductDto(value.getProductId(), value.getQuantity())).toList()).build());
            throw e;
        }
    }

    private void addOrderProducts(Order order, List<CreateOrderProductCommand> commands) {
        if (!CollectionUtils.isEmpty(commands)) {
            for (CreateOrderProductCommand command : commands) {
                ProductDto productDto = productProxy.retrieveProduct(command.getProductId());
                OrderProduct orderProduct = OrderProduct.fromCreateCommand(order, productDto, command);
                order.addOrderProduct(orderProduct);
            }

        }
    }

    public OrderDetailDto getOrder(Long id) {
        return orderRepository.findById(id).map(OrderDetailDto::from).orElseThrow(() -> new EntityNotFoundException("Order not found"));

    }
}
