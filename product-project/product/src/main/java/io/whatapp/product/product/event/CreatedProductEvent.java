package io.whatapp.product.product.event;

import io.whatapp.product.product.entity.Product;
import lombok.Getter;

import javax.persistence.EntityNotFoundException;

@Getter
public class CreatedProductEvent {
    private final Product product;

    private CreatedProductEvent(Product product) {
        this.product = product;
    }

    public static CreatedProductEvent from(Product product) {
        if (product == null) {
            throw new EntityNotFoundException("상품 정보가 존재하지 않습니다.");
        }
        return new CreatedProductEvent(product);
    }
}
