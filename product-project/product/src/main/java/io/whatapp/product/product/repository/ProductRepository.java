package io.whatapp.product.product.repository;

import io.whatapp.product.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductInfoCurrentQuantity(Long currentQuantity);
    Slice<Product> findByIdGreaterThan(Long id, Pageable pageable);
}
