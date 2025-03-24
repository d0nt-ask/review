package io.whatap.order.order.entity;

import io.whatap.library.shared.entity.BaseEntity;
import io.whatap.order.order.controller.req.CreateOrderProductCommand;
import io.whatap.order.order.entity.vo.OrderProductInfo;
import io.whatap.order.order.proxy.res.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class OrderProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    private Long productId;
    private String productName;
    @Embedded
    private OrderProductInfo orderProductInfo;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    private OrderProduct(Long productId, String productName, OrderProductInfo orderProductInfo, Order order) {
        if (productId == null) {
            throw new IllegalArgumentException("상품은 필수 입력값 입니다.");
        }
        if (order == null) {
            throw new IllegalArgumentException("주문이 올바르지 않습니다.");
        }
        this.productId = productId;
        this.productName = productName;
        this.orderProductInfo = orderProductInfo;
        this.order = order;
    }

    public static OrderProduct fromCreateCommand(Order order, ProductDto productDto, CreateOrderProductCommand command) {

        return builder()
                .order(order)
                .productId(productDto.getId())
                .productName(productDto.getName())
                .orderProductInfo(OrderProductInfo.builder()
                        .price(productDto.getPrice())
                        .quantity(command.getQuantity())
                        .build())
                .build();
    }
}
