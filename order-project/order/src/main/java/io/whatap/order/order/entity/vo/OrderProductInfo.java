package io.whatap.order.order.entity.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Getter
public class OrderProductInfo {
    private final Long quantity;
    private final Long price;
    private final Long totalPrice;

    protected OrderProductInfo() {
        this.quantity = null;
        this.price = null;
        this.totalPrice = null;
    }

    @Builder
    private OrderProductInfo(Long quantity, Long price) {
        if (quantity == null) {
            throw new IllegalArgumentException("수량은 필수 입력값입니다.");
        }
        if (price == null) {
            throw new IllegalArgumentException("가격은 필수 입력값입니다.");
        }
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = quantity * price;
    }
}
