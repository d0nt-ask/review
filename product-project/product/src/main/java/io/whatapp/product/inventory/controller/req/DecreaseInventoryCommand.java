package io.whatapp.product.inventory.controller.req;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DecreaseInventoryCommand {
    private Long productId;
    private long quantity;
}
