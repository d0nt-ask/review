package io.whatapp.product.inventory.listener.kafka;

import io.whatap.order.event.FailedOrderCreationEvent;
import io.whatapp.product.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final ApplicationEventPublisher eventPublisher;
    private final InventoryService inventoryService;

    @KafkaListener(topics = {"order"})
    public void listen(ConsumerRecord<String, Object> record) {
        eventPublisher.publishEvent(record.value());
    }

    @EventListener
    public void onFailedOrderCreation(FailedOrderCreationEvent event) {
        inventoryService.increaseInventoryQuantities(event.getOrderProducts());
    }
}
