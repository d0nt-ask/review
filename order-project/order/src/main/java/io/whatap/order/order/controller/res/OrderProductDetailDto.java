package io.whatap.order.order.controller.res;

import io.whatap.order.order.entity.Order;
import io.whatap.order.order.entity.OrderProduct;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderProductDetailDto {
    private Long id;
    private Long productId;
    private String productName;
    private long quantity;
    private long price;
    private long totalPrice;

    public static OrderProductDetailDto from(OrderProduct orderProduct) {
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
