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
}
