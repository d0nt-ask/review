package io.whatapp.product.product.service;

import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.controller.req.CreateProductImageCommand;
import io.whatapp.product.product.controller.req.UpdateProductCommand;
import io.whatapp.product.product.controller.req.UpdateProductImageCommand;
import io.whatapp.product.product.controller.res.ProductDto;
import io.whatapp.product.product.entity.Product;
import io.whatapp.product.product.entity.ProductImage;
import io.whatapp.product.product.entity.vo.ProductInfo;
import io.whatapp.product.product.event.CreatedProductEvent;
import io.whatapp.product.product.repository.ProductRepository;
import javax.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


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
    @PersistenceContext
    private EntityManager entityManager;

    public ProductDto findById(Long id) {
        return productRepository.findById(id).map(ProductDto::from).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Slice<ProductDto> findByPageable(Long id, Pageable pageable) {
        if (id == null) {
            Page<Product> products = productRepository.findAll(pageable);
            return new PageImpl<>(ProductDto.fromProducts(products.getContent()), products.getPageable(), products.getTotalElements());
        } else {
            Slice<Product> products = productRepository.findByIdGreaterThan(id, pageable);
            return new SliceImpl<>(ProductDto.fromProducts(products.getContent()), products.getPageable(), products.hasNext());
        }
    }

    public Long createProduct(CreateProductCommand command) {
        Product product = Product.from(command);
        addProductImages(product, command.getImages());

        eventPublisher.publishEvent(CreatedProductEvent.from(product));
        return product.getId();
    }

    public Long updateProduct(Long id, UpdateProductCommand command) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new EntityNotFoundException("Product not found");
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

    private void modifyProductImages(Product product, List<UpdateProductImageCommand> commands) {
        Map<UUID, ProductImage> productImageMap = product.getProductImages().stream().collect(Collectors.toMap(ProductImage::getId, Function.identity()));
        if (!CollectionUtils.isEmpty(commands)) {
            commands.forEach(command -> product.modifyProductImage(productImageMap.get(command.getProductImageId()), command.getSequence()));
        }
    }

    private void addProductImages(Product product, List<CreateProductImageCommand> command) {
        if (!CollectionUtils.isEmpty(command)) {
            List<ProductImage> productImages = ProductImage.formCreateCommands(product, command);
            productImages.forEach(product::addProductImage);
            productRepository.save(product);
        }
    }

    private void modifyProduct(Product product, UpdateProductCommand command) {
        if (command != null) {
            ProductInfo productInfo = ProductInfo.from(product, command);
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
