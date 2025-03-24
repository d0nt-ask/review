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
public class OrderInfo {
    @Enumerated(EnumType.STRING)
    private final OrderStatus status;
    private final Long totalPrice;
    private final LocalDateTime orderCreatedDateTime;
    private final LocalDateTime orderedDateTime;

    protected OrderInfo() {
        this.status = null;
        this.totalPrice = null;
        this.orderCreatedDateTime = null;
        this.orderedDateTime = null;
    }

    @Builder
    private OrderInfo(OrderStatus status, Long totalPrice, LocalDateTime orderCreatedDateTime, LocalDateTime orderedDateTime) {
        this.status = status;
        this.totalPrice = totalPrice;
        this.orderCreatedDateTime = orderCreatedDateTime;
        this.orderedDateTime = orderedDateTime;
    }

    public static OrderInfo init() {
        return builder().status(OrderStatus.DRAFT).orderCreatedDateTime(LocalDateTime.now()).build();
    }

    public OrderInfo modifyTotalPrice(Long totalPrice) {
        if (totalPrice == null) {
            throw new IllegalArgumentException("결제 금액이 올바르지 않습니다.");
        }
        return OrderInfo.builder()
                .status(this.status)
                .totalPrice(totalPrice)
                .orderCreatedDateTime(this.orderCreatedDateTime)
                .build();
    }
}
