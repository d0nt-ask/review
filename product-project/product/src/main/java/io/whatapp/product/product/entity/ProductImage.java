package io.whatapp.product.product.entity;

import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.entity.vo.ImageInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@NoArgsConstructor
public class ProductImage extends RootEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private UUID fileId;
    private String fileName;
    @Embedded
    private ImageInfo imageInfo;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    private ProductImage(Product product, UUID fileId, String fileName, ImageInfo imageInfo) {
        this.product = product;
        this.fileId = fileId;
        this.fileName = fileName;
        this.imageInfo = imageInfo;
    }

    private static ProductImage from(Product product, CreateProductCommand.CreateProductImageCommand image) {
        return builder()
                .imageInfo(new ImageInfo(image.getThumbnailUrl(), image.getOriginUrl()))
                .fileId(image.getFileId())
                .fileName(image.getFileName())
                .product(product)
                .build();
    }

    public static List<ProductImage> from(Product product, List<CreateProductCommand.CreateProductImageCommand> images) {
        return images.stream().map(image -> from(product, image)).toList();
    }
}
