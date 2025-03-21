package io.whatapp.product.product.event;

import io.whatapp.product.product.entity.Product;
import lombok.Getter;

@Getter
public class CreatedProductEvent {
    private final Product product;

    CreatedProductEvent(Product product) {
        this.product = product;
    }

    public static CreatedProductEvent from(Product product) {
        return new CreatedProductEvent(product);
    }
}
