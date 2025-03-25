package io.whatap.order.order.proxy.req;

import io.whatap.order.order.controller.req.CreateOrderProductCommand;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecreaseInventoryQuantityRequest {
    private Long productId;
    private Long quantity;

    public static DecreaseInventoryQuantityRequest from(CreateOrderProductCommand command){
        return DecreaseInventoryQuantityRequest.builder()
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .build();
    }
}
