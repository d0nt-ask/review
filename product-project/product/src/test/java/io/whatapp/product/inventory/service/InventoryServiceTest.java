package io.whatapp.product.inventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.whatapp.product.inventory.controller.req.DecreaseInventoryCommand;
import io.whatapp.product.inventory.entity.Inventory;
import io.whatapp.product.inventory.repository.InventoryRepository;
import io.whatapp.product.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@RecordApplicationEvents
@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @SpyBean
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("정상: 재고 생성")
    void initializeInventory() {
        Product product = product();

        Long id = inventoryService.initializeInventory(product);

        assertThat(inventoryRepository.findById(id)).isPresent();
    }

    @Test
    @DisplayName("정상: 재고 삭제")
    void deleteInventoryFromProduct() {
        Product product = product();
        Long inventoryId = inventoryService.initializeInventory(product);
        Inventory inventory = inventoryRepository.findById(inventoryId).get();

        Long id = inventoryService.deleteInventoryFromProduct(inventory.getProductId());

        assertThat(inventoryRepository.findById(id)).isNotPresent();
    }

    private Product product() {
        try {
            return objectMapper.readValue(
                    """
                            {
                              "id": 9999999999,
                              "productInfo": {
                              "name": "나랑드사이다 제로, 500ml, 48개",
                              "description": "원산지: 상품 상세설명 참조",
                              "price": 32680,
                              "currentQuantity": 1000
                              }
                            }
                            """, Product.class
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private DecreaseInventoryCommand decreaseInventoryCommand(Product product) {
        try {
            return objectMapper.readValue(
                    """
                            {
                              "productId": %d,
                              "quantity": 1
                              }
                            }
                            """.formatted(product.getId()), DecreaseInventoryCommand.class
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}