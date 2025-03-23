package io.whatap.order.order.entity.vo;

import io.whatap.order.order.entity.enumeration.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInfo {
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private long totalPrice;
    private LocalDateTime orderCreatedDateTime;
    private LocalDateTime orderedDateTime;

    protected OrderInfo() {
        this.status = null;
        this.totalPrice = 0;
        this.orderCreatedDateTime = LocalDateTime.now();
    }

    public static OrderInfo init() {
        return builder().status(OrderStatus.CREATED).totalPrice(0).orderCreatedDateTime(LocalDateTime.now()).build();
    }

    public OrderInfo modifyTotalPrice(long totalPrice) {
        return OrderInfo.builder()
                .status(this.status)
                .totalPrice(totalPrice)
                .orderCreatedDateTime(this.orderCreatedDateTime)
                .build();
    }
}
