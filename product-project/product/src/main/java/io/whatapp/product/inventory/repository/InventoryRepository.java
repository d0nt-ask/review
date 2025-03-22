package io.whatapp.product.inventory.repository;

import io.whatapp.product.inventory.entity.Inventory;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Inventory> findWithLockByProductId(Long productId);

    Optional<Inventory> findByProductId(Long productId);
}
