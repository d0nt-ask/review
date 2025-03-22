package io.whatapp.product.init;

import io.whatapp.product.inventory.entity.vo.Quantity;
import io.whatapp.product.product.entity.Product;
import io.whatapp.product.product.entity.ProductImage;
import io.whatapp.product.product.entity.vo.ImageInfo;
import io.whatapp.product.product.entity.vo.ProductInfo;
import io.whatapp.product.inventory.entity.Inventory;
import io.whatapp.product.product.repository.ProductRepository;
import io.whatapp.product.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @EventListener({ApplicationReadyEvent.class})
    public void init() {
        for (int i = 0; i < 100; i++) {
            Product product1 = Product.builder()
                    .productInfo(ProductInfo.builder().name("맥심 모카골드 제로슈거 커피믹스").description("원산지: 상품 상세설명 참조").price(12900L).currentQuantity(10000L).build())
                    .build();
            List.of(
                    ProductImage.builder()
                            .product(product1)
                            .fileId(UUID.randomUUID())
                            .fileName("no_img_1000_1000.png")
                            .sequence(0)
                            .imageInfo(ImageInfo.builder().thumbnailUrl("https://thumbnail7.coupangcdn.com/thumbnails/remot…40193401-d3467179-fcc6-4b1a-82c5-efe0e367df60.jpg").originUrl("https://thumbnail7.coupangcdn.com/thumbnails/remot…40193401-d3467179-fcc6-4b1a-82c5-efe0e367df60.jpg").build())
                            .build(),
                    ProductImage.builder()
                            .product(product1)
                            .fileId(UUID.randomUUID())
                            .fileName("no_img_1000_1000.png")
                            .sequence(1)
                            .imageInfo(ImageInfo.builder().thumbnailUrl("https://thumbnail7.coupangcdn.com/thumbnails/remot…40193401-d3467179-fcc6-4b1a-82c5-efe0e367df60.jpg").originUrl("https://thumbnail7.coupangcdn.com/thumbnails/remot…40193401-d3467179-fcc6-4b1a-82c5-efe0e367df60.jpg").build())
                            .build()

            ).forEach(product1::addProductImage);

            productRepository.save(product1);
            inventoryRepository.save(Inventory.builder().quantity(Quantity.builder().currentQuantity(1000L).totalQuantity(1000L).build()).productId(product1.getId()).build())
            ;
        }
        System.out.println();

    }
}
