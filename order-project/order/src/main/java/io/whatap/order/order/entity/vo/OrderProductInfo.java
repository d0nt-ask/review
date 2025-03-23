package io.whatap.order.order.entity.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderProductInfo {
    private final long quantity;
    private final long price;
    private final long totalPrice;

    protected OrderProductInfo() {
        this.quantity = 0;
        this.price = 0;
        this.totalPrice = 0;
    }
}
