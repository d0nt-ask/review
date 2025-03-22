package io.whatap.order.order.listener.application;

import io.whatap.order.event.FailedOrderCreationEvent;
import io.whatap.order.order.controller.req.CreateOrderProductCommand;
import io.whatap.order.order.event.CreatedOrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Transactional
public class OrderListener {
    @Value("${spring.application.name:order}")
    private String applicationName;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handle(CreatedOrderEvent event) {
        kafkaTemplate.send(applicationName, FailedOrderCreationEvent.builder()
                .orderProducts(event.getOrder().getOrderProducts().stream().map(orderProduct -> new FailedOrderCreationEvent.OrderProductDto(orderProduct.getProductId(), orderProduct.getQuantity())).toList()).build());
    }
}
