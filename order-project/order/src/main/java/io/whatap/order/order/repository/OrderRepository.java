package io.whatap.order.order.repository;

import io.whatap.order.order.entity.Order;
import io.whatap.order.order.entity.enumeration.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserIdAndOrderInfoStatusNot(String userId, OrderStatus orderStatus, Pageable pageable);

    Slice<Order> findByIdGreaterThanAndUserIdAndOrderInfoStatusNot(Long id, String userId, OrderStatus orderStatus, Pageable pageable);

    List<Order> findByOrderInfoOrderCreatedDateTimeLessThanAndOrderInfoStatus(LocalDateTime orderCreatedDateTime, OrderStatus orderStatus);
}
