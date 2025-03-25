package io.whatapp.product.inventory.event;

import io.whatapp.product.inventory.entity.Inventory;
import lombok.Getter;

import javax.persistence.EntityNotFoundException;

@Getter
public class IncreasedInventoryQuantityEvent {
    private Inventory inventory;

    private IncreasedInventoryQuantityEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public static IncreasedInventoryQuantityEvent from(Inventory inventory) {
        if (inventory == null) {
            throw new EntityNotFoundException("재고 정보가 존재하지 않습니다.");
        }
        return new IncreasedInventoryQuantityEvent(inventory);
    }
}
