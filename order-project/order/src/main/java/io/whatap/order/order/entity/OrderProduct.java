package io.whatap.order.order.entity;

import io.whatap.order.order.controller.req.CreateOrderProductCommand;
import io.whatap.order.order.proxy.res.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    private Long productId;
    private long quantity;
    private long totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    private OrderProduct(Long productId, long quantity, long totalPrice, Order order) {
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.order = order;
    }

    public static OrderProduct fromCreateCommand(Order order, ProductDto productDto, CreateOrderProductCommand command) {
        return builder()
                .order(order)
                .productId(productDto.getId())
                .quantity(command.getQuantity())
                .totalPrice(productDto.getPrice() * command.getQuantity())
                .build();
    }
}
