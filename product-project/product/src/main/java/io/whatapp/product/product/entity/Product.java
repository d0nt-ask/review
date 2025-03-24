package io.whatapp.product.product.entity;

import io.whatap.library.shared.entity.BaseEntity;
import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.entity.vo.ProductInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Embedded
    private ProductInfo productInfo;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<ProductImage> productImages = new ArrayList<>();

    @Builder
    private Product(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }


    public void addProductImage(ProductImage productImage) {
        this.productImages.add(productImage);
    }

    public static Product from(CreateProductCommand command) {
        return Product.builder()
                .productInfo(ProductInfo.builder().name(command.getName()).description(command.getDescription()).price(command.getPrice()).currentQuantity(command.getQuantity()).build())
                .build();
    }

    public void modifyProduct(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public void modifyProductImage(ProductImage productImage, int sequence) {

        if (this.productImages.contains(productImage)) {
            productImage.modifySequence(sequence);
        }
    }

    public void removeProductImage(ProductImage productImage) {
        this.productImages.remove(productImage);
    }

    public void remove() {
        this.productImages.clear();
    }

    public void syncProductQuantity(Long currentQuantity) {
        this.productInfo = productInfo.syncQuantity(currentQuantity);
    }
}
