package io.whatap.order.order.controller.res;

import io.whatap.order.order.entity.Order;
import io.whatap.order.order.entity.enumeration.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class OrderDetailDto {
    private Long orderId;
    private OrderStatus status;
    private long totalPrice;
    private LocalDateTime orderStartDateTime;
    private String roadAddr;
    private String jibunAddr;
    private String detailAddr;
    private List<OrderProductDetailDto> orderProducts;


    public static OrderDetailDto from(Order order) {
        return builder()
                .orderId(order.getId())
                .status(order.getOrderInfo().getStatus())
                .totalPrice(order.getOrderInfo().getTotalPrice())
                .orderStartDateTime(order.getOrderInfo().getOrderStartDateTime())
                .roadAddr(order.getAddress().getRoadAddr())
                .jibunAddr(order.getAddress().getJibunAddr())
                .detailAddr(order.getAddress().getDetailAddr())
                .orderProducts(order.getOrderProducts().stream().map(OrderProductDetailDto::from).toList())
                .build();
    }
}
