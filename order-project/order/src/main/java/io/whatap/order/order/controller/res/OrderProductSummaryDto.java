package io.whatap.order.order.controller.res;

import io.whatap.order.order.entity.OrderProduct;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderProductSummaryDto {
    private Long id;
    private Long productId;
    private String productName;
    private Long quantity;
    private Long price;
    private Long totalPrice;

    public static OrderProductSummaryDto from(OrderProduct orderProduct) {
        return builder()
                .id(orderProduct.getId())
                .productId(orderProduct.getProductId())
                .productName(orderProduct.getProductName())
                .quantity(orderProduct.getOrderProductInfo().getQuantity())
                .price(orderProduct.getOrderProductInfo().getPrice())
                .totalPrice(orderProduct.getOrderProductInfo().getTotalPrice())
                .build();
    }
}
