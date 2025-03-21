package io.whatapp.product.product.entity.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class ProductInfo {
    private String name;
    private String description;
    private Long price;
    private Long currentQuantity;

}
