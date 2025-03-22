package io.whatapp.product.product.entity.vo;

import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.controller.req.UpdateProductCommand;
import io.whatapp.product.product.entity.Product;
import io.whatapp.product.product.entity.ProductImage;

import javax.persistence.Embeddable;

import lombok.*;

//import javax.persistence.Column;
//import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
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

    public static ProductInfo from(Product product, UpdateProductCommand command) {
        return builder()
                .name(command.getName())
                .description(command.getDescription())
                .price(command.getPrice())
                .currentQuantity(product.getProductInfo().getCurrentQuantity())
                .build();
    }

    public ProductInfo syncQuantity(Long currentQuantity) {
        return builder()
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .currentQuantity(currentQuantity)
                .build();
    }
}
