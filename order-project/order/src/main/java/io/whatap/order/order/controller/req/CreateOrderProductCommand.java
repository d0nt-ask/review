package io.whatap.order.order.controller.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateOrderProductCommand {
    private Long productId;
    private long quantity;
}
