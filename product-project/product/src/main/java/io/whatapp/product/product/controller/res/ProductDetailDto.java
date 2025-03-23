package io.whatapp.product.product.controller.res;

import io.whatapp.product.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Builder
@Getter
public class ProductDetailDto {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Long quantity;
    private List<ProductImageDetailDto> images;

    public static ProductDetailDto from(Product product) {
        return builder()
                .id(product.getId())
                .name(product.getProductInfo().getName())
                .description(product.getProductInfo().getDescription())
                .price(product.getProductInfo().getPrice())
                .quantity(product.getProductInfo().getCurrentQuantity())
                .images(
                        product.getProductImages() == null
                                ? Collections.EMPTY_LIST
                                : product.getProductImages().stream().map(ProductImageDetailDto::from).toList())
                .build();
    }
}
