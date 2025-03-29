package io.whatapp.product.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.whatapp.product.inventory.entity.Inventory;
import io.whatapp.product.product.controller.req.CreateProductCommand;
import io.whatapp.product.product.controller.req.UpdateProductCommand;
import io.whatapp.product.product.controller.res.ProductDetailDto;
import io.whatapp.product.product.controller.res.ProductSummaryDto;
import io.whatapp.product.product.entity.Product;
import io.whatapp.product.product.entity.ProductImage;
import io.whatapp.product.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RecordApplicationEvents
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @SpyBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("정상: 단건 조회")
    void findById() {
        CreateProductCommand createProductCommand = createProductCommand();
        Long id = productService.createProduct(createProductCommand);

        ProductDetailDto productDetailDto = productService.findById(id);

        assertThat(productDetailDto).isNotNull();
    }

    @Test
    @DisplayName("예외: 미존재 조회")
    void findByIdException() {
        assertThrows(EntityNotFoundException.class, () -> productService.findById(Long.MAX_VALUE));

    }

    @Test
    @DisplayName("정상: 목록 조회")
    void findByPageable() {
        CreateProductCommand productCommand = createProductCommand();

        Long id1 = productService.createProduct(productCommand);
        Long id2 = productService.createProduct(productCommand);
        Long id3 = productService.createProduct(productCommand);
        Long id4 = productService.createProduct(productCommand);

        Page<ProductSummaryDto> pages = (Page<ProductSummaryDto>) productService.findByPageable(null, PageRequest.of(0, 2));
        Slice<ProductSummaryDto> slices = productService.findByPageable(id1, PageRequest.ofSize(2));

        assertThat(pages.getTotalElements()).isGreaterThan(4);
        assertThat(pages.getNumberOfElements()).isEqualTo(2);
        assertThat(slices.getNumberOfElements()).isEqualTo(2);
        assertThat(slices.getContent().get(0).getId()).isEqualTo(id2);
    }

    @Test
    @DisplayName("정상: 상품 생성")
    void createProduct() {
        CreateProductCommand createProductCommand = createProductCommand();

        Long id = productService.createProduct(createProductCommand);

        Optional<Product> productOptional = productRepository.findById(id);
        assertThat(productOptional).isPresent();
        assertThat(productOptional.get().getProductImages().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("정상: 상품 수정")
    void updateProduct() {
        CreateProductCommand createProductCommand = createProductCommand();
        Long id = productService.createProduct(createProductCommand);
        Product product = productRepository.findById(id).get();
        UpdateProductCommand updateProductCommand = updateProductCommand(product.getProductImages().get(0).getId(), product.getProductImages().get(1).getId());

        productService.updateProduct(id, updateProductCommand);

        Optional<Product> productOptional = productRepository.findById(id);
        assertThat(productOptional).isPresent();
        assertThat(productOptional.get().getProductImages().size()).isEqualTo(2);

        Optional<ProductImage> updateOptional = productOptional.get().getProductImages().stream().filter(value -> value.getId().equals(updateProductCommand.getUpdatedProductImages().get(0).getProductImageId())).findFirst();
        assertThat(updateOptional).isPresent();
        assertThat(updateOptional.get().getSequence()).isEqualTo(updateOptional.get().getSequence());

        Optional<ProductImage> deleteOptional = productOptional.get().getProductImages().stream().filter(value -> value.getFileId().equals(updateProductCommand.getDeletedProductImages().get(0))).findFirst();
        assertThat(deleteOptional).isNotPresent();

        Optional<ProductImage> createOptional = productOptional.get().getProductImages().stream().filter(value -> value.getFileId().equals(updateProductCommand.getCreatedProductImages().get(0).getFileId())).findFirst();
        assertThat(createOptional).isPresent();
    }

    @Test
    @DisplayName("예외: 미존재 상품 수정")
    void updateProductException() {
        CreateProductCommand createProductCommand = createProductCommand();
        Long id = productService.createProduct(createProductCommand);
        Product product = productRepository.findById(id).get();
        UpdateProductCommand updateProductCommand = updateProductCommand(product.getProductImages().get(0).getId(), product.getProductImages().get(1).getId());

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(Long.MAX_VALUE, updateProductCommand));
    }

    @Test
    @DisplayName("예외: 미존재 상품 이미지 수정")
    void updateProductException2() {
        CreateProductCommand createProductCommand = createProductCommand();
        Long id = productService.createProduct(createProductCommand);
        Product product = productRepository.findById(id).get();
        UpdateProductCommand updateProductCommand = updateProductCommand(UUID.randomUUID(), product.getProductImages().get(1).getId());

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(product.getId(), updateProductCommand));
    }

    @Test
    @DisplayName("정상: 상품 삭제")
    void deleteProduct() {
        CreateProductCommand createProductCommand = createProductCommand();
        Long id = productService.createProduct(createProductCommand);

        productService.deleteProduct(id);

        assertThat(productRepository.findById(id)).isNotPresent();
    }

    @Test
    void syncProductCurrentQuantity() {
        CreateProductCommand createProductCommand = createProductCommand();
        Long id = productService.createProduct(createProductCommand);
        Inventory inventory = inventory(id, 200L);


        productService.syncProductCurrentQuantity(inventory);

        Optional<Product> productOptional = productRepository.findById(id);
        assertThat(productOptional).isPresent();
        assertThat(productOptional.get().getProductInfo().getCurrentQuantity()).isEqualTo(inventory.getQuantity().getCurrentQuantity());
    }

    private Inventory inventory(Long productId, Long currentQuantity) {
        try {
            return objectMapper.readValue("""
                    {
                      "productId": "%s",
                      "quantity": {
                      "currentQuantity": %d
                      }
                    }
                    """.formatted(productId, currentQuantity), Inventory.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private CreateProductCommand createProductCommand() {
        try {
            return objectMapper.readValue("""
                    {
                      "images": [{
                        "fileId": "0f562989-d004-46f8-946b-71a6e30b701f",
                        "fileName": "1794b791-edb8-43e1-bd25-a495d350a4a41074190155312595231.png",
                        "sequence": 0,
                        "thumbnailUrl": "https://thumbnail6.coupangcdn.com/thumbnails/remote/48x48ex/image/retail/images/1794b791-edb8-43e1-bd25-a495d350a4a41074190155312595231.png",
                        "originUrl": "https://thumbnail6.coupangcdn.com/thumbnails/remote/492x492ex/image/retail/images/1794b791-edb8-43e1-bd25-a495d350a4a41074190155312595231.png"
                      },{
                        "fileId": "1f562989-d004-46f8-946b-71a6e30b701f",
                        "fileName": "1794b791-edb8-43e1-bd25-a495d350a4a41074190155312595231.png",
                        "sequence": 0,
                        "thumbnailUrl": "https://thumbnail6.coupangcdn.com/thumbnails/remote/48x48ex/image/retail/images/1794b791-edb8-43e1-bd25-a495d350a4a41074190155312595231.png",
                        "originUrl": "https://thumbnail6.coupangcdn.com/thumbnails/remote/492x492ex/image/retail/images/1794b791-edb8-43e1-bd25-a495d350a4a41074190155312595231.png"
                      }],
                      "quantity": 1000,
                      "price": 32680,
                      "name": "나랑드사이다 제로, 500ml, 48개",
                      "description": "원산지: 상품 상세설명 참조"
                    }
                    """, CreateProductCommand.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private UpdateProductCommand updateProductCommand(UUID deletedProductImageId, UUID updatedProductImageId) {
        try {
            return objectMapper.readValue("""
                    {
                      "name": "나랑드사이다 제로, 500ml, 24개",
                      "description": "원산지: 상품 상세설명 참조",
                      "price": 34000,
                      "createdProductImages": [
                        {
                          "fileId": "2f562989-d004-46f8-946b-71a6e30b701f",
                          "fileName": "1794b791-edb8-43e1-bd25-a495d350a4a41074190155312595231.png",
                          "sequence": 0,
                          "thumbnailUrl": "https://thumbnail6.coupangcdn.com/thumbnails/remote/48x48ex/image/retail/images/1794b791-edb8-43e1-bd25-a495d350a4a41074190155312595231.png",
                          "originUrl": "https://thumbnail6.coupangcdn.com/thumbnails/remote/492x492ex/image/retail/images/1794b791-edb8-43e1-bd25-a495d350a4a41074190155312595231.png"
                        }
                      ],
                      "updatedProductImages": [
                        {
                          "productImageId": "%s",
                          "sequence": 999
                        }
                      ],
                      "deletedProductImages": [
                        "%s"
                      ]
                    }
                    """.formatted(updatedProductImageId.toString(), deletedProductImageId.toString()), UpdateProductCommand.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}