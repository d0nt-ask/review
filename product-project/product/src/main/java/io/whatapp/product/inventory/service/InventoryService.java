package io.whatapp.product.inventory.service;

import io.whatap.order.event.FailedOrderCreationEvent;
import io.whatapp.product.inventory.controller.req.DecreaseInventoryCommand;
import io.whatapp.product.inventory.entity.Inventory;
import io.whatapp.product.inventory.event.DecreasedInventoryQuantityEvent;
import io.whatapp.product.inventory.repository.InventoryRepository;
import io.whatapp.product.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Long initializeInventory(Product product) {
        Inventory inventory = Inventory.from(product);
        inventoryRepository.save(inventory);
        return inventory.getId();
    }

    public Long deleteInventoryFromProduct(Long productId) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);
        inventoryOptional.ifPresent(inventoryRepository::delete);
        return productId;
    }

    public List<Long> decreaseInventoryQuantities(List<DecreaseInventoryCommand> commands) {
        List<Long> ids = new ArrayList<>();
        for (DecreaseInventoryCommand command : commands) {
            ids.add(decreaseInventoryQuantity(command));
        }
        return ids;
    }

    public void increaseInventoryQuantities(List<FailedOrderCreationEvent.OrderProductDto> orderProducts) {
        for (FailedOrderCreationEvent.OrderProductDto orderProduct : orderProducts) {
            increaseInventoryQuantity(orderProduct);
        }

    }

    private Long decreaseInventoryQuantity(DecreaseInventoryCommand command) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findWithLockByProductId(command.getProductId());
        Inventory inventory = inventoryOptional.orElseThrow(() -> new EntityNotFoundException("Inventory not found"));
        inventory.decreaseQuantity(command.getQuantity());
        inventoryRepository.save(inventory);
        eventPublisher.publishEvent(DecreasedInventoryQuantityEvent.from(inventory));
        return inventory.getId();
    }

    private void increaseInventoryQuantity(FailedOrderCreationEvent.OrderProductDto orderProduct) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findWithLockByProductId(orderProduct.getProductId());
        Inventory inventory = inventoryOptional.orElseThrow(() -> new EntityNotFoundException("Inventory not found"));
        inventory.increaseQuantity(orderProduct.getQuantity());
        inventoryRepository.save(inventory);
        eventPublisher.publishEvent(DecreasedInventoryQuantityEvent.from(inventory));
    }

}
