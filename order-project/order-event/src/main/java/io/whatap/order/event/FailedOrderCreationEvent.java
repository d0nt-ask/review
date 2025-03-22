package io.whatap.order.event;


import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailedOrderCreationEvent {

    private List<OrderProductDto> orderProducts;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductDto {
        private Long productId;
        private Long quantity;
    }
}
