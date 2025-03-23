package io.whatap.order.order.listener.application;

import io.whatap.order.event.DeletedOrderEvent;
import io.whatap.order.event.FailedOrderCreationEvent;
import io.whatap.order.event.dto.OrderProductDto;
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
                .orderProducts(event.getOrder().getOrderProducts().stream().map(orderProduct -> new OrderProductDto(orderProduct.getProductId(), orderProduct.getOrderProductInfo().getQuantity())).toList()).build());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handle(FailedOrderCreationEvent event) {
        kafkaTemplate.send(applicationName, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(DeletedOrderEvent event) {
        kafkaTemplate.send(applicationName, event);
    }
}
