package io.whatapp.product.inventory.event;

import io.whatapp.product.inventory.entity.Inventory;
import lombok.Getter;

@Getter
public class IncreasedInventoryQuantityEvent {
    private Inventory inventory;

    private IncreasedInventoryQuantityEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public static IncreasedInventoryQuantityEvent from(Inventory inventory) {
        return new IncreasedInventoryQuantityEvent(inventory);
    }
}
