package io.whatapp.product.inventory.service;

import io.whatapp.product.inventory.entity.Inventory;
import io.whatapp.product.inventory.repository.InventoryRepository;
import io.whatapp.product.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {
    private final InventoryRepository inventoryRepository;

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
}
