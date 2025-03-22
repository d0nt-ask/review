package io.whatapp.product.product.listener.application;

import io.whatapp.product.inventory.event.DecreasedInventoryQuantityEvent;
import io.whatapp.product.inventory.event.IncreasedInventoryQuantityEvent;
import io.whatapp.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class InventoryEventListener {
    private final ProductService productService;

    @EventListener
    public void handle(DecreasedInventoryQuantityEvent event) {
        productService.syncProductCurrentQuantity(event.getInventory());
    }

    @EventListener
    public void handle(IncreasedInventoryQuantityEvent event) {
        productService.syncProductCurrentQuantity(event.getInventory());
    }
}
