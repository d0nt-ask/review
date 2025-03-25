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
    @OrderBy("sequence ASC")
    private final List<ProductImage> productImages = new ArrayList<>();

    @Builder
    private Product(ProductInfo productInfo) {
        if (productInfo == null) {
            throw new IllegalArgumentException("상품 정보는 필수 입력값입니다.");
        }
        this.productInfo = productInfo;
    }


    public void addProductImage(ProductImage productImage) {
        if (productInfo == null) {
            throw new IllegalArgumentException("상품 사진은 필수 입력값입니다.");
        }
        this.productImages.add(productImage);
    }

    public static Product from(CreateProductCommand command) {
        return Product.builder()
                .productInfo(ProductInfo.builder().name(command.getName()).description(command.getDescription()).price(command.getPrice()).currentQuantity(command.getQuantity()).build())
                .build();
    }

    public void modifyProduct(ProductInfo productInfo) {
        if (productInfo == null) {
            throw new IllegalArgumentException("상품 정보는 필수 입력값입니다.");
        }
        this.productInfo = productInfo;
    }

    public void modifyProductImage(ProductImage productImage, int sequence) {
        if (this.productImages.contains(productImage)) {
            productImage.modifySequence(sequence);
        } else {
            throw new EntityNotFoundException("상품 사진 정보가 존재하지 않습니다.");
        }
    }

    public void removeProductImage(ProductImage productImage) {
        if (!this.productImages.remove(productImage)) {
            throw new EntityNotFoundException("상품 사진 정보가 존재하지 않습니다.");
        }
    }

    public void remove() {
        this.productImages.clear();
    }

    public void syncProductQuantity(Long currentQuantity) {
        this.productInfo = productInfo.syncQuantity(currentQuantity);
    }
}
