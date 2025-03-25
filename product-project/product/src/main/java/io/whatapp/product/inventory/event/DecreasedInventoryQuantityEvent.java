package io.whatapp.product.inventory.event;

import io.whatapp.product.inventory.entity.Inventory;
import lombok.Getter;

import javax.persistence.EntityNotFoundException;

@Getter
public class DecreasedInventoryQuantityEvent {
    private final Inventory inventory;

    private DecreasedInventoryQuantityEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public static DecreasedInventoryQuantityEvent from(Inventory inventory) {
        if (inventory == null) {
            throw new EntityNotFoundException("재고 정보가 존재하지 않습니다.");
        }
        return new DecreasedInventoryQuantityEvent(inventory);
    }
}
