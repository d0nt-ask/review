package io.whatapp.product.product.entity;

import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.entity.vo.ProductInfo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Product extends RootEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Embedded
    private ProductInfo productInfo;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private final List<ProductImage> productImages = new ArrayList<>();

    @Builder
    private Product(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }


    public void addProductImage(List<ProductImage> productImages) {
        this.productImages.addAll(productImages);
    }

    public static Product from(CreateProductCommand command) {
        return Product.builder()
                .productInfo(ProductInfo.builder().name(command.getName()).description(command.getDescription()).price(command.getPrice()).currentQuantity(command.getQuantity()).build())
                .build();
    }
}
