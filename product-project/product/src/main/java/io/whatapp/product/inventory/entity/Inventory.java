package io.whatapp.product.inventory.entity;

import io.whatap.library.shared.entity.BaseEntity;
import io.whatapp.product.inventory.entity.vo.Quantity;
import io.whatapp.product.product.entity.Product;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Inventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    private Long productId;
    private Quantity quantity;

    @Builder
    private Inventory(Long productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static Inventory from(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("상품은 필수 입력값입니다.");
        }
        return Inventory.builder()
                .productId(product.getId())
                .quantity(
                        Quantity.builder()
                                .currentQuantity(product.getProductInfo().getCurrentQuantity())
                                .totalQuantity(product.getProductInfo().getCurrentQuantity())
                                .build())
                .build();
    }


    public void decreaseQuantity(Long quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("수량은 필수 입력값입니다.");
        }
        this.quantity = this.quantity.decreaseCurrentQuantity(quantity);
    }

    public void increaseQuantity(Long quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("수량은 필수 입력값입니다.");
        }
        this.quantity = this.quantity.increaseCurrentQuantity(quantity);
    }
}
