package io.whatapp.product.product.service;

import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.controller.req.UpdateProductCommand;
import io.whatapp.product.product.controller.res.ProductDto;
import io.whatapp.product.product.entity.Product;
import io.whatapp.product.product.entity.ProductImage;
import io.whatapp.product.product.event.CreatedProductEvent;
import io.whatapp.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

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
        List<ProductImage> productImages = ProductImage.from(product, command.getImages());
        product.addProductImage(productImages);

        productRepository.save(product);

        eventPublisher.publishEvent(CreatedProductEvent.from(product));
        return product.getId();
    }

    public Long updateProduct(Long id, UpdateProductCommand command) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new EntityNotFoundException("Product not found");
        } else {
//            optionalProduct
//                    .get()
//                    .updateProductFromCommand(command)
        }
        return null;
    }
}
