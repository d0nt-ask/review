package io.whatap.order.order.controller.res;

import io.whatap.order.order.entity.Order;
import io.whatap.order.order.entity.enumeration.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
public class OrderSummaryDto {
    private Long orderId;
    private OrderStatus status;
    private Long totalPrice;
    private LocalDateTime orderedDateTime;
    private List<OrderProductSummaryDto> orderProducts;

    public static OrderSummaryDto from(Order order) {
        return builder()
                .orderId(order.getId())
                .status(order.getOrderInfo().getStatus())
                .totalPrice(order.getOrderInfo().getTotalPrice())
                .orderedDateTime(order.getOrderInfo().getOrderedDateTime())
                .orderProducts(
                        CollectionUtils.isEmpty(order.getOrderProducts())
                                ? Collections.EMPTY_LIST
                                : order.getOrderProducts().stream().map(OrderProductSummaryDto::from).toList()
                )
                .build();
    }
}
