package io.whatapp.product.product.event;

import io.whatapp.product.product.entity.Product;
import lombok.Getter;

@Getter
public class DeletedProductEvent {
    private final Product product;

    private DeletedProductEvent(Product product) {
        this.product = product;
    }

    public static DeletedProductEvent from(Product product) {
        return new DeletedProductEvent(product);
    }
}
