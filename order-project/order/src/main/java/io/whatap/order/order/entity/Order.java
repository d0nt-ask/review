package io.whatap.order.order.entity;

import io.whatap.order.order.controller.req.CreateOrderCommand;
import io.whatap.order.order.controller.req.CreateOrderProductCommand;
import io.whatap.order.order.entity.enumeration.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "`order`")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    protected Order(OrderStatus status) {
        this.status = status;
    }

    public static Order from(CreateOrderCommand command) {
        return Order.builder().status(OrderStatus.Creation).build();
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }
}
