package io.whatapp.product.product.entity;

import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.entity.vo.ProductInfo;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private void deleteProductImages(List<UUID> deletedProductImages) {
        if (!CollectionUtils.isEmpty(deletedProductImages)) {
            List<ProductImage> imagesToDelete = this.productImages.stream()
                    .filter(image -> deletedProductImages.contains(image.getId()))
                    .toList();
            this.productImages.removeAll(imagesToDelete);
        }
    }

    public void modifyProductImage(ProductImage productImage, int sequence) {
        int i = this.productImages.indexOf(productImage);
        if (i >= 0) {
            productImage.modifySequence(sequence);
        }
    }

    public void removeProductImage(ProductImage productImage) {
        this.productImages.remove(productImage);
    }
}
