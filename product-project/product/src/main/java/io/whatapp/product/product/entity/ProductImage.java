package io.whatapp.product.product.entity;

import io.whatapp.product.product.controller.req.CreateProductImageCommand;
import io.whatapp.product.product.controller.req.UpdateProductImageCommand;
import io.whatapp.product.product.entity.vo.ImageInfo;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    private int sequence;
    @Embedded
    private ImageInfo imageInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    private ProductImage(Product product, UUID fileId, String fileName, int sequence, ImageInfo imageInfo) {
        this.product = product;
        this.fileId = fileId;
        this.fileName = fileName;
        this.sequence = sequence;
        this.imageInfo = imageInfo;
    }

    public static ProductImage formCreateCommand(Product product, CreateProductImageCommand command) {
        return builder()
                .imageInfo(ImageInfo.builder().thumbnailUrl(command.getThumbnailUrl()).originUrl(command.getOriginUrl()).build())
                .fileId(command.getFileId())
                .fileName(command.getFileName())
                .sequence(command.getSequence())
                .product(product)
                .build();
    }

    public void modifySequence(int sequence) {
        this.sequence = sequence;
    }
}
