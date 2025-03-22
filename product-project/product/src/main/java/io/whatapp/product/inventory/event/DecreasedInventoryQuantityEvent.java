package io.whatapp.product.inventory.event;

import io.whatapp.product.inventory.entity.Inventory;
import lombok.Getter;

@Getter
public class DecreasedInventoryQuantityEvent {
    private Inventory inventory;

    private DecreasedInventoryQuantityEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public static DecreasedInventoryQuantityEvent from(Inventory inventory) {
        return new DecreasedInventoryQuantityEvent(inventory);
    }
}
