package io.whatap.order.order.controller.res;

import io.whatap.order.order.entity.Order;
import io.whatap.order.order.entity.enumeration.OrderStatus;
import io.whatap.order.order.entity.vo.Address;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Builder
@Getter
public class OrderDetailDto {
    private Long orderId;
    private OrderStatus status;
    private Long totalPrice;
    private LocalDateTime orderCreatedDateTime;
    private LocalDateTime orderDateTime;
    private String roadAddr;
    private String jibunAddr;
    private String detailAddr;
    private List<OrderProductDetailDto> orderProducts;


    public static OrderDetailDto from(Order order) {
        return builder()
                .orderId(order.getId())
                .status(order.getOrderInfo().getStatus())
                .totalPrice(order.getOrderInfo().getTotalPrice())
                .orderCreatedDateTime(order.getOrderInfo().getOrderCreatedDateTime())
                .roadAddr(Optional.ofNullable(order.getAddress()).map(Address::getRoadAddr).orElse(null))
                .jibunAddr(Optional.ofNullable(order.getAddress()).map(Address::getJibunAddr).orElse(null))
                .detailAddr(Optional.ofNullable(order.getAddress()).map(Address::getDetailAddr).orElse(null))
                .orderProducts(order.getOrderProducts().stream().map(OrderProductDetailDto::from).toList())
                .build();
    }
}
