package io.whatapp.product.inventory.entity;

import io.whatapp.product.inventory.entity.vo.Quantity;
import io.whatapp.product.product.entity.Product;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Inventory {
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


    public void decrease(Long count) {
//        currentQuantity = currentQuantity - count;
    }


    public static Inventory from(Product product) {
        return Inventory.builder()
                .productId(product.getId())
                .quantity(
                        Quantity.builder()
                                .currentQuantity(product.getProductInfo().getCurrentQuantity())
                                .totalQuantity(product.getProductInfo().getCurrentQuantity())
                                .build())
                .build();
    }
}
