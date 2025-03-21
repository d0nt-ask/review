package io.whatapp.product.product.controller.res;

import io.whatapp.product.product.entity.ProductImage;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class ProductImageDto {
    private UUID id;
    private String fileName;
    private String thumbnailUrl;
    private String originUrl;


    public static ProductImageDto from(ProductImage productImage) {
        return builder()
                .id(productImage.getId())
                .fileName(productImage.getFileName())
                .thumbnailUrl(productImage.getImageInfo() != null ? productImage.getImageInfo().getThumbnailUrl() : null)
                .originUrl(productImage.getImageInfo() != null ? productImage.getImageInfo().getOriginUrl() : null)
                .build();
    }
}
