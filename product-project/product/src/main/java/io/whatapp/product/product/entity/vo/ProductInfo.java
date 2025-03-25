package io.whatapp.product.product.entity.vo;

import javax.persistence.Embeddable;

import lombok.*;
import org.apache.commons.lang3.StringUtils;


@Embeddable
@Getter
public class ProductInfo {
    private final String name;
    private final String description;
    private final Long price;
    private final Long currentQuantity;

    protected ProductInfo() {
        name = null;
        description = null;
        price = null;
        currentQuantity = null;
    }

    @Builder
    private ProductInfo(String name, String description, Long price, Long currentQuantity) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("상품명은 필수 입력값입니다.");
        }
        if (price == null) {
            throw new IllegalArgumentException("가격은 필수 입력값입니다.");
        }
        if (currentQuantity == null) {
            throw new IllegalArgumentException("재고는 필수 입력값입니다.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("가격은 0 이상이여야 합니다");
        }
        if (currentQuantity < 0) {
            throw new IllegalArgumentException("재고는 0 이상이여야 합니다");
        }
        this.name = name;
        this.description = description;
        this.price = price;
        this.currentQuantity = currentQuantity;
    }

    public ProductInfo syncQuantity(Long currentQuantity) {
        if (currentQuantity == null) {
            throw new IllegalArgumentException("재고는 필수 입력값입니다.");
        }
        if (currentQuantity < 0) {
            throw new IllegalArgumentException("재고는 0 이상이여야 합니다");
        }
        return builder()
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .currentQuantity(currentQuantity)
                .build();
    }
}
