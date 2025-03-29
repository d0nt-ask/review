package io.whatapp.product.inventory.service;

import com.zaxxer.hikari.HikariDataSource;
import io.whatap.library.shared.lock.annotation.DistributedLocks;
import io.whatap.order.event.dto.OrderProductDto;
import io.whatapp.product.inventory.controller.req.DecreaseInventoryCommand;
import io.whatapp.product.inventory.entity.Inventory;
import io.whatapp.product.inventory.event.DecreasedInventoryQuantityEvent;
import io.whatapp.product.inventory.repository.InventoryRepository;
import io.whatapp.product.product.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    private HikariDataSource dataSource;


    @Transactional
    public Long initializeInventory(Product product) {
        Inventory inventory = Inventory.from(product);
        inventoryRepository.save(inventory);
        return inventory.getId();
    }

    @Transactional
    public Long deleteInventoryFromProduct(Long productId) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);
        inventoryOptional.ifPresent(inventoryRepository::delete);
        return productId;
    }

    @Transactional
    public List<Long> decreaseInventoryQuantities(List<DecreaseInventoryCommand> commands) {
        List<Long> ids = new ArrayList<>();
        for (DecreaseInventoryCommand command : commands) {
            ids.add(decreaseInventoryQuantity(command));
        }
        return ids;
    }

    @Transactional
    @DistributedLocks(lockName = "inventory", keys = "#commands.![productId]")
    public List<Long> decreaseInventoryQuantitiesV2(List<DecreaseInventoryCommand> commands) {
        StringBuilder status = new StringBuilder();
        status.append("Pool Name: ").append(dataSource.getPoolName()).append("\n");
        status.append("Active Connections: ").append(dataSource.getHikariPoolMXBean().getActiveConnections()).append("\n");
        status.append("Idle Connections: ").append(dataSource.getHikariPoolMXBean().getIdleConnections()).append("\n");
        status.append("Total Connections: ").append(dataSource.getHikariPoolMXBean().getTotalConnections()).append("\n");
        status.append("Threads Awaiting Connection: ").append(dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()).append("\n");
        log.info(status.toString());

        List<Long> ids = new ArrayList<>();

        for (DecreaseInventoryCommand command : commands) {
            ids.add(decreaseInventoryQuantityV2(command));
        }
        return ids;
    }

    @Transactional
    @DistributedLocks(lockName = "inventory", keys = "#orderProducts.![productId]")
    public void increaseInventoryQuantities(List<OrderProductDto> orderProducts) {
        for (OrderProductDto orderProduct : orderProducts) {
            increaseInventoryQuantity(orderProduct);
        }
    }

    private Long decreaseInventoryQuantity(DecreaseInventoryCommand command) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findWithLockByProductId(command.getProductId());
        Inventory inventory = inventoryOptional.orElseThrow(() -> new EntityNotFoundException("해당 재고 정보가 존재하지 않습니다."));
        inventory.decreaseQuantity(command.getQuantity());
        inventoryRepository.save(inventory);
        eventPublisher.publishEvent(DecreasedInventoryQuantityEvent.from(inventory));
        return inventory.getId();
    }

    private Long decreaseInventoryQuantityV2(DecreaseInventoryCommand command) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(command.getProductId());
        Inventory inventory = inventoryOptional.orElseThrow(() -> new EntityNotFoundException("해당 재고 정보가 존재하지 않습니다."));
        inventory.decreaseQuantity(command.getQuantity());
        inventoryRepository.save(inventory);
        eventPublisher.publishEvent(DecreasedInventoryQuantityEvent.from(inventory));
        return inventory.getId();
    }

    private void increaseInventoryQuantity(OrderProductDto orderProduct) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findWithLockByProductId(orderProduct.getProductId());
        Inventory inventory = inventoryOptional.orElseThrow(() -> new EntityNotFoundException("해당 재고 정보가 존재하지 않습니다."));
        inventory.increaseQuantity(orderProduct.getQuantity());
        inventoryRepository.save(inventory);
        eventPublisher.publishEvent(DecreasedInventoryQuantityEvent.from(inventory));
    }

}
