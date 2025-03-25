package io.whatapp.product.inventory.entity.vo;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@Getter
public class Quantity {
    private Long totalQuantity;
    private Long currentQuantity;

    @Builder
    private Quantity(Long totalQuantity, Long currentQuantity) {
        if (totalQuantity == null) {
            throw new IllegalArgumentException("누적 재고는 필수 입력값입니다.");
        }

        if (currentQuantity == null) {
            throw new IllegalArgumentException("현재 재고는 필수 입력값입니다.");
        }

        if (totalQuantity < 0) {
            throw new IllegalArgumentException("누적 재고은 0개 이상이여야 합니다.");
        }

        if (currentQuantity < 0) {
            throw new IllegalArgumentException("현재 재고은 0개 이상이여야 합니다.");
        }
        this.totalQuantity = totalQuantity;
        this.currentQuantity = currentQuantity;
    }

    public Quantity decreaseCurrentQuantity(Long quantity) {
        if (currentQuantity >= quantity) {
            return builder().totalQuantity(totalQuantity).currentQuantity(currentQuantity - quantity).build();
        }
        throw new IllegalStateException("상품 재고가 부족합니다.");
    }

    public Quantity increaseCurrentQuantity(Long quantity) {
        return builder().totalQuantity(totalQuantity).currentQuantity(currentQuantity + quantity).build();
    }
}
