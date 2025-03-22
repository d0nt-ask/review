package io.whatapp.product.inventory.entity.vo;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Quantity {
    private Long totalQuantity;
    private Long currentQuantity;

    public Quantity decreaseCurrentQuantity(Long quantity) {
        if (currentQuantity >= quantity) {
            return builder().totalQuantity(totalQuantity).currentQuantity(currentQuantity - quantity).build();
        }
        throw new IllegalStateException("Cannot decrease quantity");
    }

    public Quantity increaseCurrentQuantity(Long quantity) {
        return builder().totalQuantity(totalQuantity).currentQuantity(currentQuantity + quantity).build();
    }
}
