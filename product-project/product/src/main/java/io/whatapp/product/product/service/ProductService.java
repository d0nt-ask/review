package io.whatapp.product.product.service;

import io.whatapp.product.inventory.entity.Inventory;
import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.controller.req.CreateProductImageCommand;
import io.whatapp.product.product.controller.req.UpdateProductCommand;
import io.whatapp.product.product.controller.req.UpdateProductImageCommand;
import io.whatapp.product.product.controller.res.ProductDetailDto;
import io.whatapp.product.product.controller.res.ProductSummaryDto;
import io.whatapp.product.product.entity.Product;
import io.whatapp.product.product.entity.ProductImage;
import io.whatapp.product.product.entity.vo.ProductInfo;
import io.whatapp.product.product.event.CreatedProductEvent;
import io.whatapp.product.product.event.DeletedProductEvent;
import io.whatapp.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ProductDetailDto findById(Long id) {
        return productRepository.findById(id).map(ProductDetailDto::from).orElseThrow(() -> new EntityNotFoundException("상품 정보가 존재하지 않습니다."));
    }

    public Slice<ProductSummaryDto> findByPageable(Long id, Pageable pageable) {
        if (id == null) {
            Page<Product> products = productRepository.findAll(pageable);
            return new PageImpl<>(products.getContent().stream().map(ProductSummaryDto::from).toList(), products.getPageable(), products.getTotalElements());
        } else {
            Slice<Product> products = productRepository.findByIdGreaterThan(id, pageable);
            return new SliceImpl<>(products.getContent().stream().map(ProductSummaryDto::from).toList(), products.getPageable(), products.hasNext());
        }
    }

    public Long createProduct(CreateProductCommand command) {
        Product product = Product.from(command);
        addProductImages(product, command.getImages());

        productRepository.save(product);
        eventPublisher.publishEvent(CreatedProductEvent.from(product));
        return product.getId();
    }

    public Long updateProduct(Long id, UpdateProductCommand command) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new EntityNotFoundException("상품 정보가 존재하지 않습니다.");
        } else {
            Product product = optionalProduct.get();
            modifyProduct(product, command);
            addProductImages(product, command.getCreatedProductImages());
            modifyProductImages(product, command.getUpdatedProductImages());
            removeProductImages(product, command.getDeletedProductImages());
            productRepository.save(product);
            return product.getId();
        }
    }

    public Long deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new EntityNotFoundException("상품 정보가 존재하지 않습니다.");
        } else {
            Product product = optionalProduct.get();
            product.remove();
            productRepository.delete(product);

            eventPublisher.publishEvent(DeletedProductEvent.from(product));
            return product.getId();
        }
    }

    public void syncProductCurrentQuantity(Inventory inventory) {
        Optional<Product> productOptional = productRepository.findById(inventory.getProductId());
        productOptional
                .orElseThrow(() -> new EntityNotFoundException("상품 정보가 존재하지 않습니다."))
                .syncProductQuantity(inventory.getQuantity().getCurrentQuantity());

    }


    private void modifyProductImages(Product product, List<UpdateProductImageCommand> commands) {
        if (!CollectionUtils.isEmpty(commands)) {
            Map<UUID, ProductImage> productImageMap = product.getProductImages().stream().collect(Collectors.toMap(ProductImage::getId, Function.identity()));

            commands.forEach(command -> product.modifyProductImage(productImageMap.get(command.getProductImageId()), command.getSequence()));
        }
    }

    private void addProductImages(Product product, List<CreateProductImageCommand> commands) {
        if (!CollectionUtils.isEmpty(commands)) {
            List<ProductImage> productImages = commands.stream().map(command -> ProductImage.formCreateCommand(product, command)).toList();
            productImages.forEach(product::addProductImage);
            productRepository.save(product);
        }
    }

    private void modifyProduct(Product product, UpdateProductCommand command) {
        if (command != null) {
            ProductInfo productInfo = ProductInfo.builder()
                    .name(command.getName())
                    .description(command.getDescription())
                    .price(command.getPrice())
                    .currentQuantity(product.getProductInfo().getCurrentQuantity())
                    .build();
            product.modifyProduct(productInfo);
        }
    }

    private void removeProductImages(Product product, List<UUID> productImageIds) {
        Map<UUID, ProductImage> productImageMap = product.getProductImages().stream().collect(Collectors.toMap(ProductImage::getId, Function.identity()));

        if (!CollectionUtils.isEmpty(productImageIds)) {
            productImageIds.forEach(id -> product.removeProductImage(productImageMap.get(id)));

        }
    }
}
