package io.whatapp.product.inventory.listener.application;

import io.whatapp.product.inventory.service.InventoryService;
import io.whatapp.product.product.event.CreatedProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductEventListener {

    private final InventoryService inventoryService;

    @EventListener
    @Transactional
    public void handle(CreatedProductEvent createdProductEvent) {
        inventoryService.initializeInventory(createdProductEvent.getProduct());
    }
}
