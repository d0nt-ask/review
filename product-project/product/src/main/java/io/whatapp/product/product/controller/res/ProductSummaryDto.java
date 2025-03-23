package io.whatapp.product.product.controller.res;

import io.whatapp.product.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Builder
@Getter
public class ProductSummaryDto {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Long quantity;
    private String thumbnailUrl;

    public static ProductSummaryDto from(Product product) {
        return builder()
                .id(product.getId())
                .name(product.getProductInfo().getName())
                .description(product.getProductInfo().getDescription())
                .price(product.getProductInfo().getPrice())
                .quantity(product.getProductInfo().getCurrentQuantity())
                .thumbnailUrl(CollectionUtils.isEmpty(product.getProductImages()) ? null : product.getProductImages().get(0).getImageInfo().getThumbnailUrl())
                .build();
    }
}
